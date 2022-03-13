package dev.weazyexe.reko.data.firebase.firestore

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import dev.weazyexe.reko.data.firebase.auth.AuthDataSource
import dev.weazyexe.reko.data.firebase.firestore.entity.EmotionEntity
import dev.weazyexe.reko.data.firebase.firestore.entity.ImageEntity
import dev.weazyexe.reko.domain.Emotion
import dev.weazyexe.reko.domain.RecognizerType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data source of recognized images
 */
@Singleton
class ImagesDataSource @Inject constructor(
    authDataSource: AuthDataSource
) {

    private val firestore by lazy { FirebaseFirestore.getInstance() }

    val path = "${authDataSource.getUserId()}/info/recognized"

    /**
     * Gets recognized images list from firebase
     *
     * @return list of images
     */
    suspend fun getImages(): Flow<List<ImageEntity>> = flow {
        val imagesSnapshot = firestore.collection(path)
            .orderBy("recognize_time", Query.Direction.DESCENDING)
            .get()
            .await()

        emit(imagesSnapshot.documents.mapNotNull { it.toObject() })
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
    ): Flow<ImageEntity> = flow {
        val id = UUID.randomUUID().toString()
        val entity = ImageEntity(
            id = id,
            emotions = emotions.toList().map { EmotionEntity(it.first.type, it.second) },
            image = imageUrl,
            timestamp = Timestamp(recognizeTime),
            recognizerName = recognizer.type
        )
        firestore.document("$path/$id").set(entity).await()
        emit(entity)
    }
}