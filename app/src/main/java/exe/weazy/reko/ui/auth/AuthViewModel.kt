package exe.weazy.reko.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import exe.weazy.reko.data.ApiKeyRepository
import exe.weazy.reko.data.AuthRepository
import exe.weazy.reko.data.SettingsRepository
import exe.weazy.reko.di.App
import exe.weazy.reko.model.RecognizerName
import exe.weazy.reko.state.ScreenState
import exe.weazy.reko.util.extensions.isValidLogin
import exe.weazy.reko.util.extensions.isValidPassword
import exe.weazy.reko.util.extensions.subscribe
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class AuthViewModel : ViewModel() {

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var apiKeyRepository: ApiKeyRepository

    @Inject
    lateinit var settingsRepository: SettingsRepository

    private lateinit var signInDisposable: Disposable
    private lateinit var signUpDisposable: Disposable

    val signInState = MutableLiveData(ScreenState.DEFAULT)
    val signUpState = MutableLiveData(ScreenState.DEFAULT)

    var errorMessage: String? = null

    init {
        App.getComponent().inject(this)
    }

    fun checkAccount() {
        if (authRepository.isSignedIn()) {
            signInState.postValue(ScreenState.SUCCESS)
        }
    }

    fun signIn(login: String, password: String) {
        if (validateLogin(login) && validatePassword(password)) {
            signInState.postValue(ScreenState.LOADING)

            if (::signInDisposable.isInitialized) {
                signInDisposable.dispose()
            }

            val observable = authRepository.signIn(login, password)
                .flatMap { apiKeyRepository.updateApiKeys() }

            signInDisposable = subscribe(observable, {
                apiKeyRepository.saveApplicationKey(it.application_key)
                apiKeyRepository.saveApplicationSecretKey(it.application_secret_key)
                settingsRepository.saveRecognizer(RecognizerName.SKY_BIOMETRY)

                signInState.postValue(ScreenState.SUCCESS)
            }, {
                errorMessage = it.message
                signInState.postValue(ScreenState.ERROR)
            })
        } else {
            signInState.postValue(ScreenState.ERROR)
        }
    }

    fun signUp(login: String, password: String, confirm: String) {
        if (validateLogin(login) && validatePassword(password) && password == confirm) {
            signUpState.postValue(ScreenState.LOADING)

            if (::signUpDisposable.isInitialized) {
                signUpDisposable.dispose()
            }

            val observable = authRepository.signUp(login, password)
                .flatMap { apiKeyRepository.updateApiKeys() }

            signUpDisposable = subscribe(observable, {
                apiKeyRepository.saveApplicationKey(it.application_key)
                apiKeyRepository.saveApplicationSecretKey(it.application_secret_key)
                settingsRepository.saveRecognizer(RecognizerName.SKY_BIOMETRY)

                signUpState.postValue(ScreenState.SUCCESS)
            }, {
                errorMessage = it.message
                signUpState.postValue(ScreenState.ERROR)
            })
        } else {
            errorMessage = "Passwords mismatch"
            signUpState.postValue(ScreenState.ERROR)
        }
    }

    fun validateLogin(text: String) = text.isValidLogin()

    fun validatePassword(text: String) = text.isValidPassword()
}