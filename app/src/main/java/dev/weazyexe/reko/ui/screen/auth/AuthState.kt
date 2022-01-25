package dev.weazyexe.reko.ui.screen.auth

import dev.weazyexe.core.ui.Action
import dev.weazyexe.core.ui.Effect
import dev.weazyexe.core.ui.LoadState
import dev.weazyexe.core.ui.State
import dev.weazyexe.core.utils.EMPTY_STRING

data class AuthState(
    val email: String = EMPTY_STRING,
    val password: String = EMPTY_STRING,
    val emailError: String? = null,
    val passwordError: String? = null,
    val signInLoadState: LoadState<Unit> = LoadState()
) : State

sealed class AuthEffect : Effect {

    object Initial : AuthEffect()
    object GoToMainScreen : AuthEffect()
}

sealed class AuthAction : Action {

    object OnSignInClick : AuthAction()

    data class OnEmailChange(val email: String) : AuthAction()
    data class OnPasswordChange(val password: String) : AuthAction()
}