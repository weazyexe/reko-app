package exe.weazy.reko.recognizer.tensorflow

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PointF
import android.media.FaceDetector
import exe.weazy.reko.util.extensions.pixelColor
import exe.weazy.reko.util.extensions.resetConfig
import exe.weazy.reko.util.extensions.toBlackWhite
import exe.weazy.reko.util.getEmotion
import org.tensorflow.lite.Interpreter
import java.io.File

// jeez!
// Входной тип данных в модель [n, 48, 48, 1]
// Поэтому тут происходит вот такое:
typealias InputType = Array<Pixels>
typealias OutputType = Array<FloatArray?>

typealias Pixels = Array<Array<FloatArray>>

class EmotionClassifier(context: Context) {

    private val width = 48
    private val height = 48
    private val modelPath = "model.tflite"
    private val model = Interpreter(loadModelFile(context))

    /**
     * Классифицирует эмоции лица на изображении
     */
    fun classify(bitmap: Bitmap): Map<String, Int>? {
        val faces = getFaces(bitmap = bitmap)
        if (faces.isNotEmpty()) {
            val startFacePoint = getStartPoint(face = faces[0])
            val croppedBitmap = Bitmap.createBitmap(bitmap,
                startFacePoint.x.toInt(), startFacePoint.y.toInt(), width, height)

            val input = prepareData(croppedBitmap)
            val output: OutputType = Array(1) {
                return@Array FloatArray(7) {
                    return@FloatArray 0f
                }
            }
            model.run(input, output)

            // Преобразуем вывод из нейросети в нормальный вид
            if (output.isNotEmpty()) {
                return output[0]?.mapIndexed { index, it ->
                    getEmotion(index) to (it * 100).toInt()
                }?.filter { it.second != 0 }?.toMap()
            }
        }

        return null
    }

    /**
     * Загружает модель из ассетов приложения, сохраняет и возвращает файл модели
     */
    private fun loadModelFile(context: Context): File {
        val inputStream = context.assets.open(modelPath)
        val readBytes = inputStream.readBytes()
        val file = File(context.filesDir, modelPath)
        if (!file.exists()) {
            file.writeBytes(readBytes)
        }
        return file
    }

    /**
     * Распознает лицо на изображении
     */
    private fun getFaces(bitmap: Bitmap): Array<FaceDetector.Face> {
        // Распознаем одно лицо
        val image = bitmap.resetConfig(Bitmap.Config.RGB_565)
        val faceDetector = FaceDetector(image.width, image.height, 1)
        val faces: Array<FaceDetector.Face?> = Array(1) {
            return@Array null
        }
        faceDetector.findFaces(image, faces) // faces - out параметр, в нем результат
        return faces.filterNotNull().toTypedArray()
    }

    /**
     * Найти точку, от которой начинается прямоугольник с лицом (левый верхний угол)
     */
    private fun getStartPoint(face: FaceDetector.Face): PointF {
        val midFacePoint = PointF(0f ,0f)
        face.getMidPoint(midFacePoint)

        // Приблизительно начало прямоугольника с лицом
        return PointF(midFacePoint.x - 24, midFacePoint.y - 24)
    }

    /**
     * Преобразование изображения в понятный для модели вид
     */
    private fun prepareData(bitmap: Bitmap): InputType {
        val image: MutableList<Array<FloatArray>> = mutableListOf()
        for (y in 0 until height) {
            val row = mutableListOf<FloatArray>()
            for (x in 0 until width) {
                val value = bitmap.pixelColor(x, y).toBlackWhite().red()
                row.add(FloatArray(1) {
                    return@FloatArray value
                })
            }
            image.add(row.toTypedArray())
        }
        return arrayOf(image.toTypedArray())
    }
}