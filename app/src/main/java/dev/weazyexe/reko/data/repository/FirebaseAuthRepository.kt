package dev.weazyexe.reko.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dev.weazyexe.reko.data.error.UserDoesNotExistException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Repository for interaction with Firebase Authentication
 */
class FirebaseAuthRepository {

    private val auth by lazy { FirebaseAuth.getInstance() }

    /**
     * Check user for log in status
     *
     * @return if user has already logged then returns true else false
     */
    fun checkUser(): Boolean = auth.currentUser != null

    /**
     * Sign in to Firebase Auth
     * @return firebase user
     * @throws UserDoesNotExistException if user does not exists
     */
    suspend fun signIn(email: String, password: String): FirebaseUser =
        withContext(Dispatchers.IO) {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user

            if (user != null) {
                return@withContext user
            } else {
                throw UserDoesNotExistException()
            }
        }
}