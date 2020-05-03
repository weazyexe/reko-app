package exe.weazy.reko.data

import exe.weazy.reko.data.network.NetworkService
import exe.weazy.reko.data.network.requests.LoginPasswordRequest
import exe.weazy.reko.di.App
import javax.inject.Inject

class AuthRepository {

    @Inject
    lateinit var service: NetworkService

    init {
        App.getComponent().injectAuthRepository(this)
    }

    fun signIn(login: String, password: String) = service.signIn(LoginPasswordRequest(login, password))
}