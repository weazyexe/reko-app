package dev.weazyexe.reko.ui.screen.auth

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.weazyexe.core.ui.CoreViewModel
import dev.weazyexe.core.ui.LoadState
import dev.weazyexe.core.utils.providers.StringsProvider
import dev.weazyexe.reko.data.repository.FirebaseAuthRepository
import dev.weazyexe.reko.ui.screen.auth.AuthAction.OnEmailChange
import dev.weazyexe.reko.ui.screen.auth.AuthAction.OnPasswordChange
import dev.weazyexe.reko.ui.screen.auth.AuthEffect.GoToMainScreen
import dev.weazyexe.reko.ui.screen.auth.error.AuthErrorMapper
import dev.weazyexe.reko.ui.screen.auth.validator.EmailValidator
import dev.weazyexe.reko.ui.screen.auth.validator.PasswordValidator
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val stringsProvider: StringsProvider,
    private val emailValidator: EmailValidator,
    private val passwordValidator: PasswordValidator
) : CoreViewModel<AuthState, AuthEffect, AuthAction>(), AuthErrorMapper {

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

        emailValidator.validate(email)?.let {
            state.copy(emailError = it).emit()
            return false
        }

        passwordValidator.validate(password)?.let {
            state.copy(passwordError = it).emit()
            return false
        }

        return true
    }

    private suspend fun signIn(email: String, password: String) {
        state.copy(signInLoadState = LoadState.loading()).emit()
        query(
            query = { firebaseAuthRepository.signIn(email, password) },
            onSuccess = {
                state.copy(signInLoadState = LoadState.data(Unit)).emit()
                GoToMainScreen.emit()
            },
            onError = {
                state.copy(
                    signInLoadState = LoadState.error(it),
                    passwordError = stringsProvider.getString(mapError(it).message)
                ).emit()
            }
        )
    }
}