package dev.weazyexe.reko.data.repository

import dev.weazyexe.core.network.transformCollection
import dev.weazyexe.reko.data.firebase.firestore.ImagesDataSource
import dev.weazyexe.reko.domain.RecognizedImage
import kotlinx.coroutines.flow.Flow
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
    suspend fun getImages(): Flow<List<RecognizedImage>> =
        imagesDataSource.getImages().transformCollection()
}