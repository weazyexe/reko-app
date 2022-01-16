package dev.weazyexe.reko.ui.screen.auth

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.weazyexe.core.ui.CoreViewModel
import dev.weazyexe.reko.ui.screen.auth.AuthAction.OnUsernameChange
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : CoreViewModel<AuthState, AuthEffect, AuthAction>() {

    override val initialState: AuthState = AuthState()

    override suspend fun handleAction(action: AuthAction) {
        when (action) {
            is OnUsernameChange -> state.copy(
                username = action.username
            ).emit()
            is AuthAction.OnPasswordChange -> state.copy(
                password = action.password
            ).emit()
        }
    }
}