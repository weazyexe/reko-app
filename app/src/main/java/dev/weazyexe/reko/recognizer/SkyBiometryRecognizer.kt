package dev.weazyexe.reko.recognizer

import android.content.Context
import android.net.Uri
import dev.weazyexe.reko.data.network.SkyBiometryApi
import dev.weazyexe.reko.domain.RecognizedImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

    companion object {

        private const val FILE_PREFIX = "REKO_"
    }

    override fun recognize(imageUri: Uri): Flow<RecognizedImage> = flow {
        val file = imageUri.copyAsFile()

        val filePart = MultipartBody.Part.createFormData(
            "urls",
            file.name,
            RequestBody.create(null, file)
        )

        emit(skyBiometryApi.recognize(filePart).transform())
    }

    private fun Uri.copyAsFile(): File {
        context.contentResolver.openInputStream(this).use {
            val bytes = it?.readBytes()
            val file = File.createTempFile(FILE_PREFIX, null)
            file.writeBytes(bytes ?: ByteArray(0))
            return file
        }
    }
}