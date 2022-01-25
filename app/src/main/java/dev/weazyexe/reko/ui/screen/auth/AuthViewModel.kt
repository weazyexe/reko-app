package dev.weazyexe.reko.ui.screen.auth

import android.util.Patterns
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.weazyexe.core.ui.CoreViewModel
import dev.weazyexe.core.ui.LoadState
import dev.weazyexe.core.utils.extensions.handleErrors
import dev.weazyexe.core.utils.providers.StringsProvider
import dev.weazyexe.reko.R
import dev.weazyexe.reko.data.error.UserDoesNotExistError
import dev.weazyexe.reko.data.repository.FirebaseAuthRepository
import dev.weazyexe.reko.ui.screen.auth.AuthAction.OnEmailChange
import dev.weazyexe.reko.ui.screen.auth.AuthAction.OnPasswordChange
import dev.weazyexe.reko.ui.screen.auth.AuthEffect.GoToMainScreen
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val sp: StringsProvider
) : CoreViewModel<AuthState, AuthEffect, AuthAction>() {

    override val initialState: AuthState = AuthState()

    override suspend fun handleAction(action: AuthAction) {
        when (action) {
            is OnEmailChange -> state.copy(
                email = action.email,
                emailError = null
            ).emit()
            is OnPasswordChange -> state.copy(
                password = action.password,
                passwordError = null
            ).emit()
            is AuthAction.OnSignInClick -> onSignInClick()
        }
    }

    private suspend fun onSignInClick() {
        val email = state.email
        val password = state.password

        if (validate(email, password)) {
            signIn(email, password)
        }
    }

    private suspend fun validate(email: String, password: String): Boolean {
        state.copy(emailError = null, passwordError = null).emit()

        getEmailError(email)?.let {
            state.copy(emailError = it).emit()
            return false
        }

        getPasswordError(password)?.let {
            state.copy(passwordError = it).emit()
            return false
        }

        return true
    }

    private fun getEmailError(email: String): String? {
        return when {
            email.isEmpty() -> sp.string(R.string.auth_email_is_empty_error_text)
            !email.matches(Patterns.EMAIL_ADDRESS.toRegex()) -> sp.string(R.string.auth_email_is_incorrect_error_text)
            else -> null
        }
    }

    private fun getPasswordError(password: String): String? {
        return when {
            password.isEmpty() -> sp.string(R.string.auth_password_is_empty_error_text)
            else -> null
        }
    }

    private suspend fun signIn(email: String, password: String) {
        state.copy(signInLoadState = LoadState.loading()).emit()
        firebaseAuthRepository.signIn(email, password)
            .handleErrors { handleSignInError(it) }
            .collectLatest {
                state.copy(signInLoadState = LoadState.data(Unit)).emit()
                GoToMainScreen.emit()
            }
    }

    private suspend fun handleSignInError(exception: Exception) {
        when (exception) {
            is FirebaseAuthInvalidUserException,
            is FirebaseAuthInvalidCredentialsException,
            is UserDoesNotExistError -> state.copy(
                passwordError = sp.string(R.string.auth_wrong_credentials_error_text),
            )
            else -> state.copy(
                passwordError = sp.string(R.string.auth_unknown_error_text)
            )
        }.copy(signInLoadState = LoadState.error(exception)).emit()
    }
}