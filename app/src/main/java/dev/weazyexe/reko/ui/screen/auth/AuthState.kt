package dev.weazyexe.reko.ui.screen.auth

import dev.weazyexe.core.ui.Action
import dev.weazyexe.core.ui.Effect
import dev.weazyexe.core.ui.State
import dev.weazyexe.core.utils.EMPTY_STRING

data class AuthState(
    val username: String = EMPTY_STRING,
    val password: String = EMPTY_STRING
) : State

sealed class AuthEffect : Effect {

    object Initial : AuthEffect()
    data class ShowMessage(val message: String) : AuthEffect()
}

sealed class AuthAction : Action {

    data class OnUsernameChange(val username: String) : AuthAction()
    data class OnPasswordChange(val password: String) : AuthAction()
}