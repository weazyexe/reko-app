package dev.weazyexe.reko.ui.screen.auth

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.weazyexe.core.ui.CoreViewModel
import dev.weazyexe.core.ui.LoadState
import dev.weazyexe.core.utils.providers.StringsProvider
import dev.weazyexe.reko.data.repository.FirebaseAuthRepository
import dev.weazyexe.reko.ui.common.error.ResponseError
import dev.weazyexe.reko.ui.screen.auth.AuthAction.*
import dev.weazyexe.reko.ui.screen.auth.AuthEffect.GoToMainScreen
import dev.weazyexe.reko.ui.screen.auth.error.AuthErrorMapper
import dev.weazyexe.reko.ui.screen.auth.validator.EmailValidator
import dev.weazyexe.reko.ui.screen.auth.validator.PasswordValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val stringsProvider: StringsProvider,
    private val emailValidator: EmailValidator,
    private val passwordValidator: PasswordValidator
) : CoreViewModel<AuthState, AuthEffect, AuthAction>(), AuthErrorMapper {

    override val initialState: AuthState = AuthState()

    override suspend fun onAction(action: AuthAction) {
        when (action) {
            is OnEmailChange -> setState {
                copy(
                    email = action.email,
                    emailError = null
                )
            }
            is OnPasswordChange -> setState {
                copy(
                    password = action.password,
                    passwordError = null
                )
            }
            is OnSignInClick -> onSignInClick()
        }
    }

    private suspend fun onSignInClick() {
        val email = state.email
        val password = state.password

        if (validate(email, password)) {
            signIn(email, password)
        }
    }

    private fun validate(email: String, password: String): Boolean {
        setState { copy(emailError = null, passwordError = null) }

        emailValidator.validate(email)?.let {
            setState { copy(emailError = it) }
            return false
        }

        passwordValidator.validate(password)?.let {
            setState { copy(passwordError = it) }
            return false
        }

        return true
    }

    private suspend fun signIn(email: String, password: String) {
        setState { copy(signInLoadState = LoadState.loading()) }
        firebaseAuthRepository.signIn(email, password)
            .flowOn(Dispatchers.IO)
            .onEach {
                setState { copy(signInLoadState = LoadState.data()) }
                GoToMainScreen.emit()
            }
            .catch {
                val error = mapError(it)
                val message = stringsProvider.getString(error.message)

                if (error is ResponseError.WrongCredentialsError) {
                    setState {
                        copy(
                            signInLoadState = LoadState.error(it),
                            passwordError = message
                        )
                    }
                } else {
                    setState { copy(signInLoadState = LoadState.error(it)) }
                    AuthEffect.ShowMessage(message).emit()
                }

            }
            .collect()
    }
}