package dev.weazyexe.reko.data.firebase.storage

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import dev.weazyexe.reko.data.error.FirebaseDocumentUploadException
import dev.weazyexe.reko.data.error.SessionInvalidException
import dev.weazyexe.reko.data.firebase.auth.AuthDataSource
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageDataSource @Inject constructor(
    authDataSource: AuthDataSource
) {

    private val userId = authDataSource.getUserId()

    private val storage = FirebaseStorage.getInstance()

    /**
     * Saves document [uri] with [extension]
     *
     * @return URL to the document at Firebase Storage
     */
    suspend fun saveDocument(uri: Uri, extension: String): String {
        if (userId.isNullOrBlank()) {
            throw SessionInvalidException()
        }

        val fileName = UUID.randomUUID().toString()
        val reference = storage.getReference("$userId/$fileName.$extension")
        try {
            val result = reference.putFile(uri).await()
            if (result.task.isSuccessful) {
                return reference.downloadUrl.await().toString()
            } else {
                throw Exception()
            }
        } catch (e: Exception) {
            throw FirebaseDocumentUploadException()
        }
    }
}