package exe.weazy.reko.data

import com.google.firebase.auth.FirebaseAuth
import exe.weazy.reko.data.firebase.RxFirebase

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    fun signIn(email: String, password: String) = RxFirebase.signIn(email, password)

    fun signUp(email: String, password: String) = RxFirebase.signUp(email, password)

    fun isSignedIn() = auth.currentUser != null
}