package dev.weazyexe.reko.ui.screen.main

import dev.weazyexe.core.ui.Action
import dev.weazyexe.core.ui.Effect
import dev.weazyexe.core.ui.LoadState
import dev.weazyexe.core.ui.State
import dev.weazyexe.reko.domain.RecognizedImage

/**
 * State for [MainScreen]
 */
data class MainState(
    val imagesLoadState: LoadState<List<RecognizedImage>> = LoadState(data = emptyList())
) : State

/**
 * Side effects for [MainScreen]
 */
sealed class MainEffect : Effect

/**
 * Actions for [MainScreen]
 */
sealed class MainAction : Action {

    /**
     * On recognize button click action
     */
    object OnRecognizeClick : MainAction()
}

