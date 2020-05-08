package exe.weazy.reko.data

import exe.weazy.reko.data.firebase.RxFirebase
import exe.weazy.reko.data.network.NetworkService
import exe.weazy.reko.data.network.requests.LoginPasswordRequest
import exe.weazy.reko.di.App
import javax.inject.Inject

class AuthRepository {
    fun signIn(email: String, password: String) = RxFirebase.signIn(email, password)
}