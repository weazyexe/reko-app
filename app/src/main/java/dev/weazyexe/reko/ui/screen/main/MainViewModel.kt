package dev.weazyexe.reko.ui.screen.main

import dev.weazyexe.core.ui.CoreViewModel

/**
 * ViewModel for [MainScreen]
 */
class MainViewModel : CoreViewModel<MainState, MainEffect, MainAction>() {

    override val initialState: MainState = MainState()

    override suspend fun onAction(action: MainAction) {

    }

}