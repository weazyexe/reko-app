package dev.weazyexe.reko.recognizer

import android.content.Context
import android.graphics.Bitmap
import dev.weazyexe.reko.data.error.FacesNotFoundException
import dev.weazyexe.reko.data.network.SkyBiometryApi
import dev.weazyexe.reko.domain.RecognizedImage
import dev.weazyexe.reko.utils.flowIo
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * Emotion recognizer via SkyBiometry service
 */
class SkyBiometryRecognizer(
    private val skyBiometryApi: SkyBiometryApi,
    private val context: Context
) : Recognizer {

    override fun recognize(bitmap: Bitmap): Flow<RecognizedImage> = flowIo {
        val file = bitmap.saveToFile()
        val filePart = MultipartBody.Part.createFormData(
            "urls",
            file.name,
            RequestBody.create(null, file)
        )

        val recognizedImage = skyBiometryApi.recognize(filePart)
        if (recognizedImage.photos.firstOrNull()?.tags.isNullOrEmpty()) {
            throw FacesNotFoundException()
        }

        recognizedImage.transform()
    }

    private fun Bitmap.saveToFile(): File =
        File(context.cacheDir, generateFileName()).apply {
            outputStream().use {
                compress(Bitmap.CompressFormat.JPEG, 85, it)
                it.flush()
            }
        }

    private fun generateFileName(): String = "IMG_${System.currentTimeMillis()}.jpeg"
}