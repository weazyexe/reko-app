package exe.weazy.reko.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import exe.weazy.reko.data.AuthRepository
import exe.weazy.reko.state.ScreenState
import exe.weazy.reko.util.extensions.isValidLogin
import exe.weazy.reko.util.extensions.isValidPassword
import exe.weazy.reko.util.extensions.subscribe
import io.reactivex.disposables.Disposable

class LoginViewModel : ViewModel() {

    private val repository = AuthRepository()
    private lateinit var signInDisposable: Disposable

    val state = MutableLiveData(ScreenState.DEFAULT)

    fun signIn(login: String, password: String) {
        if (validateLogin(login) && validatePassword(password)) {
            state.postValue(ScreenState.LOADING)

            if (::signInDisposable.isInitialized) {
                signInDisposable.dispose()
            }
            signInDisposable = subscribe(repository.signIn(login, password), {
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