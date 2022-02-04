package dev.weazyexe.reko.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dev.weazyexe.reko.data.error.UserDoesNotExistException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseAuthRepository {

    private val auth by lazy { FirebaseAuth.getInstance() }

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