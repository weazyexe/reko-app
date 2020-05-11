package exe.weazy.reko.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import exe.weazy.reko.data.ApiKeyRepository
import exe.weazy.reko.data.AuthRepository
import exe.weazy.reko.di.App
import exe.weazy.reko.state.ScreenState
import exe.weazy.reko.util.extensions.isValidLogin
import exe.weazy.reko.util.extensions.isValidPassword
import exe.weazy.reko.util.extensions.subscribe
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class LoginViewModel : ViewModel() {

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var apiKeyRepository: ApiKeyRepository

    private lateinit var signInDisposable: Disposable

    val state = MutableLiveData(ScreenState.DEFAULT)

    init {
        App.getComponent().inject(this)
    }

    fun checkAccount() {
        if (authRepository.isSignedIn()) {
            state.postValue(ScreenState.SUCCESS)
        }
    }

    fun signIn(login: String, password: String) {
        if (validateLogin(login) && validatePassword(password)) {
            state.postValue(ScreenState.LOADING)

            if (::signInDisposable.isInitialized) {
                signInDisposable.dispose()
            }

            val observable = authRepository.signIn(login, password)
                .flatMap { apiKeyRepository.updateApiKeys() }

            signInDisposable = subscribe(observable, {
                apiKeyRepository.saveApplicationKey(it.application_key)
                apiKeyRepository.saveApplicationSecretKey(it.application_secret_key)

                state.postValue(ScreenState.SUCCESS)
            }, {
                state.postValue(ScreenState.ERROR)
            })
        } else {
            state.postValue(ScreenState.ERROR)
        }
    }

    fun validateLogin(text: String) = text.isValidLogin()

    fun validatePassword(text: String) = text.isValidPassword()
}