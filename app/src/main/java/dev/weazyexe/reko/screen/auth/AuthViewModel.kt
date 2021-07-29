package dev.weazyexe.reko.screen.auth

import dev.weazyexe.reko.core.ui.CoreViewModel
import dev.weazyexe.reko.core.ui.ViewState

class AuthViewModel : CoreViewModel() {

    override val initialState: ViewState
        get() = AuthViewState()

}