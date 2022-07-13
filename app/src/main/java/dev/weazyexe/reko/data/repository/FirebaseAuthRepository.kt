package dev.weazyexe.reko.data.repository

import com.google.firebase.auth.FirebaseUser
import dev.weazyexe.reko.data.error.UserDoesNotExistException
import dev.weazyexe.reko.data.firebase.auth.AuthDataSource
import dev.weazyexe.reko.utils.flowIo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for interaction with Firebase Authentication
 */
@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val authDataSource: AuthDataSource
) {

    /**
     * Check user for log in status
     *
     * @return if user has already logged then returns true else false
     */
    fun checkUser(): Boolean = authDataSource.checkUser()

    /**
     * Sign in to Firebase Auth
     *
     * @return firebase user
     * @throws UserDoesNotExistException if user does not exists
     */
    suspend fun signIn(email: String, password: String): Flow<FirebaseUser> = flowIo {
        authDataSource.signIn(email, password)
    }
}