package dev.weazyexe.reko.data.firebase.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dev.weazyexe.reko.data.error.UserDoesNotExistException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data source to work with Firebase Authentication
 */
@Singleton
class AuthDataSource @Inject constructor() {

    private val auth by lazy { FirebaseAuth.getInstance() }

    /**
     * Check user for log in status
     *
     * @return if user has already logged then returns true else false
     */
    fun checkUser(): Boolean = auth.currentUser != null

    /**
     * Gets current user ID
     */
    fun getUserId(): String? = auth.currentUser?.uid

    /**
     * Sign in to Firebase Auth
     *
     * @return firebase user
     * @throws UserDoesNotExistException if user does not exists
     */
    suspend fun signIn(email: String, password: String): FirebaseUser {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val user = result.user

        if (user != null) {
            return user
        } else {
            throw UserDoesNotExistException()
        }
    }
}