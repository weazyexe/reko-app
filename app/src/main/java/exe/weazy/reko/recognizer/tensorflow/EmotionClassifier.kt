package exe.weazy.reko.recognizer.tensorflow

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PointF
import android.media.FaceDetector
import androidx.core.graphics.get
import exe.weazy.reko.util.getEmotion
import org.tensorflow.lite.Interpreter
import java.io.File

// jeez!
// Входной тип данных в модель [n, 48, 48, 1]
// Поэтому тут происходит вот такое:
typealias InputType = Array<Pixels>
typealias OutputType = Array<Array<Float>>

typealias Pixels = MutableList<Array<Array<Float>>>

class EmotionClassifier(context: Context) {

    private val width = 48
    private val height = 48
    private val modelPath = "model.tflite"
    private val model = Interpreter(loadModelFile(context))

    fun classify(bitmap: Bitmap): Map<String, Int>? {
        val faces = getFaces(bitmap = bitmap)
        if (faces.isNotEmpty()) {
            val startFacePoint = getStartPoint(face = faces[0])
            val croppedBitmap = Bitmap.createBitmap(bitmap,
                startFacePoint.x.toInt(), startFacePoint.y.toInt(), width, height)

            val input = prepareData(croppedBitmap)
            val output: OutputType = arrayOf()
            model.run(input, output)

            if (output.isNotEmpty()) {
                return output[0].mapIndexed { index, it ->
                    getEmotion(index) to (it * 100).toInt()
                }.toMap()
            }
        }

        return null
    }

    private fun loadModelFile(context: Context): File {
        val inputStream = context.assets.open(modelPath)
        val readBytes = inputStream.readBytes()
        val file = File(context.filesDir, modelPath)
        if (!file.exists()) {
            file.writeBytes(readBytes)
        }
        return file
    }

    private fun getFaces(bitmap: Bitmap): Array<FaceDetector.Face> {
        // Распознаем одно лицо
        val faceDetector = FaceDetector(bitmap.width, bitmap.height, 2)
        val faces: Array<FaceDetector.Face> = arrayOf()
        faceDetector.findFaces(bitmap, faces) // faces - out параметр, в нем результат
        return faces
    }

    private fun getStartPoint(face: FaceDetector.Face): PointF {
        val midFacePoint = PointF(0f ,0f)
        face.getMidPoint(midFacePoint)

        // Приблизительно начало прямоугольника с лицом
        return PointF(midFacePoint.x - 24, midFacePoint.y - 24)
    }

    private fun prepareData(bitmap: Bitmap): InputType {
        val image: Pixels = mutableListOf()
        for (y in 0 until height) {
            val row = mutableListOf<Array<Float>>()
            for (x in 0 until width) {
                row.add(arrayOf(bitmap[x, y].toFloat()))
            }
            image.add(row.toTypedArray())
        }
        return arrayOf(image)
    }
}