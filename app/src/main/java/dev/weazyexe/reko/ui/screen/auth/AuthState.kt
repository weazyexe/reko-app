package dev.weazyexe.reko.ui.screen.auth

import dev.weazyexe.core.ui.Action
import dev.weazyexe.core.ui.Effect
import dev.weazyexe.core.ui.LoadState
import dev.weazyexe.core.ui.State
import dev.weazyexe.core.utils.EMPTY_STRING
import dev.weazyexe.reko.ui.screen.main.MainScreen

/**
 * State for [AuthScreen]
 */
data class AuthState(
    val email: String = EMPTY_STRING,
    val password: String = EMPTY_STRING,
    val emailError: String? = null,
    val passwordError: String? = null,
    val signInLoadState: LoadState<Unit> = LoadState()
) : State

/**
 * Side effects for [AuthScreen]
 */
sealed class AuthEffect : Effect {

    /**
     * Navigation to [MainScreen]
     */
    object GoToMainScreen : AuthEffect()

    /**
     * Show message in snackbar side effect
     */
    data class ShowMessage(val message: String) : AuthEffect()
}

/**
 * Available actions at [AuthScreen]
 */
sealed class AuthAction : Action {

    /**
     * On sign in button click action
     */
    object OnSignInClick : AuthAction()

    /**
     * On email field change action
     */
    data class OnEmailChange(val email: String) : AuthAction()

    /**
     * On password field change action
     */
    data class OnPasswordChange(val password: String) : AuthAction()
}