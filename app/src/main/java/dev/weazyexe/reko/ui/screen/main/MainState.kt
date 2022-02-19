package dev.weazyexe.reko.ui.screen.main

import android.graphics.Bitmap
import dev.weazyexe.core.ui.Action
import dev.weazyexe.core.ui.Effect
import dev.weazyexe.core.ui.LoadState
import dev.weazyexe.core.ui.State
import dev.weazyexe.reko.domain.RecognizedImage

/**
 * State for [MainScreen]
 */
data class MainState(
    val imagesLoadState: LoadState<List<RecognizedImage>> = LoadState()
) : State

/**
 * Side effects for [MainScreen]
 */
sealed class MainEffect : Effect {

    /**
     * Open screen with recognized image details
     */
    data class OpenImageScreen(val image: RecognizedImage) : MainEffect()

    /**
     * Show error snackbar on the screen
     */
    data class ShowErrorMessage(val message: String) : MainEffect()

    /**
     * Show default snackbar on the screen
     */
    data class ShowMessage(val message: String) : MainEffect()
}

/**
 * Actions for [MainScreen]
 */
sealed class MainAction : Action {

    /**
     * Recognize emotion on the [bitmap]
     */
    data class RecognizeEmotions(val bitmap: Bitmap) : MainAction()
}

