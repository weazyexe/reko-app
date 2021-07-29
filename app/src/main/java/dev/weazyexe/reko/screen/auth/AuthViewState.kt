package dev.weazyexe.reko.screen.auth

import dev.weazyexe.reko.core.ui.ViewState
import dev.weazyexe.reko.core.utils.EMPTY_STRING

data class AuthViewState(
    val username: String = EMPTY_STRING,
    val password: String = EMPTY_STRING
): ViewState