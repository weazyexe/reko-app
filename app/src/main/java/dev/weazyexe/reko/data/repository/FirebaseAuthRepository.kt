package dev.weazyexe.reko.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dev.weazyexe.reko.data.error.UserDoesNotExistException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for interaction with Firebase Authentication
 */
@Singleton
class FirebaseAuthRepository @Inject constructor() {

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
    suspend fun signIn(email: String, password: String): Flow<FirebaseUser> = flow {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val user = result.user

        if (user != null) {
            emit(user)
        } else {
            throw UserDoesNotExistException()
        }
    }
}