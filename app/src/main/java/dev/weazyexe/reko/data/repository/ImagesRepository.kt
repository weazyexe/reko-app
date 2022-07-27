package dev.weazyexe.reko.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.core.net.toUri
import coil.imageLoader
import coil.request.ImageRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.weazyexe.core.network.transformCollection
import dev.weazyexe.reko.data.error.FirebaseDocumentUploadException
import dev.weazyexe.reko.data.firebase.firestore.ImagesDataSource
import dev.weazyexe.reko.data.firebase.storage.StorageDataSource
import dev.weazyexe.reko.domain.Emotion
import dev.weazyexe.reko.domain.RecognizedImage
import dev.weazyexe.reko.domain.RecognizerType
import dev.weazyexe.reko.utils.flowIo
import kotlinx.coroutines.flow.Flow
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Repository for interaction with images store
 */
@Singleton
class ImagesRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val imagesDataSource: ImagesDataSource,
    private val storageDataSource: StorageDataSource
) {

    companion object {

        private const val DEFAULT_IMAGE_EXTENSION = "jpeg"
    }

    /**
     * Gets recognized images list from firebase
     *
     * @return list of images
     */
    suspend fun getImages(): Flow<List<RecognizedImage>> = flowIo {
        imagesDataSource.getImages().transformCollection()
    }

    /**
     * Saves recognized image to Firebase Firestore
     *
     * @return created image entity
     */
    suspend fun saveImage(
        imageUrl: String,
        emotions: Map<Emotion, Int>,
        recognizeTime: Date,
        recognizer: RecognizerType
    ): Flow<RecognizedImage> = flowIo {
        imagesDataSource
            .saveImage(imageUrl, emotions, recognizeTime, recognizer)
            .transform()
    }

    /**
     * Saves document from [url] to Firebase Storage
     *
     * @return URL at Firebase Storage
     */
    fun saveDocumentByUrl(url: String): Flow<String> = flowIo {
        val bitmap = downloadImage(url)
        val encodedImage = bitmap.toByteArray()
        val uri = saveImageLocally(encodedImage)
        storageDataSource.saveDocument(uri, DEFAULT_IMAGE_EXTENSION)
    }

    private suspend fun downloadImage(url: String): Bitmap {
        val request = ImageRequest.Builder(context)
            .data(url)
            .build()
        return (context.imageLoader.execute(request).drawable as? BitmapDrawable)?.bitmap
            ?: throw FirebaseDocumentUploadException()
    }

    private fun saveImageLocally(bytes: ByteArray): Uri {
        val tempId = UUID.randomUUID().toString()
        val file = File(context.cacheDir, tempId)
        file.writeBytes(bytes)
        return file.toUri()
    }

    private fun Bitmap.toByteArray(): ByteArray {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 90, stream)
        return stream.toByteArray()
    }
}