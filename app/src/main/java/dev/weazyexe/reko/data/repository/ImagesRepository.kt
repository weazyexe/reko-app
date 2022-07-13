package dev.weazyexe.reko.data.repository

import dev.weazyexe.core.network.transformCollection
import dev.weazyexe.reko.data.firebase.firestore.ImagesDataSource
import dev.weazyexe.reko.domain.Emotion
import dev.weazyexe.reko.domain.RecognizedImage
import dev.weazyexe.reko.domain.RecognizerType
import dev.weazyexe.reko.utils.flowIo
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for interaction with images store
 */
@Singleton
class ImagesRepository @Inject constructor(
    private val imagesDataSource: ImagesDataSource
) {

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
}