package dev.weazyexe.reko.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dev.weazyexe.reko.data.error.UserDoesNotExistError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository {

    private val auth by lazy { FirebaseAuth.getInstance() }

    suspend fun signIn(email: String, password: String): Flow<FirebaseUser> = flow {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val user = result.user
        if (user != null) {
            emit(user)
        } else {
            throw UserDoesNotExistError()
        }
    }
}