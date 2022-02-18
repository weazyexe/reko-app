package dev.weazyexe.reko.data.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import dev.weazyexe.reko.data.firebase.auth.AuthDataSource
import dev.weazyexe.reko.data.firebase.firestore.entity.ImageEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
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
        val imagesSnapshot = firestore.collection(path).get().await()
        emit(imagesSnapshot.documents.mapNotNull { it.toObject<ImageEntity>() })
    }
}