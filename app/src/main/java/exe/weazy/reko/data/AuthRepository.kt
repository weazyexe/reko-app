package exe.weazy.reko.data

import exe.weazy.reko.data.firebase.RxFirebase

class AuthRepository {
    fun signIn(email: String, password: String) = RxFirebase.signIn(email, password)
}