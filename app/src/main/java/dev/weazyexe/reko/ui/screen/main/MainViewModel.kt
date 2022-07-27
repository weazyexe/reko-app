package dev.weazyexe.reko.ui.screen.main

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.weazyexe.core.ui.CoreViewModel
import dev.weazyexe.core.utils.extensions.data
import dev.weazyexe.core.utils.extensions.error
import dev.weazyexe.core.utils.extensions.loading
import dev.weazyexe.core.utils.providers.StringsProvider
import dev.weazyexe.reko.data.repository.ImagesRepository
import dev.weazyexe.reko.recognizer.Recognizer
import dev.weazyexe.reko.ui.common.error.ResponseError
import dev.weazyexe.reko.ui.screen.main.MainAction.*
import dev.weazyexe.reko.ui.screen.main.MainEffect.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for [MainScreen]
 */
@FlowPreview
@HiltViewModel
class MainViewModel @Inject constructor(
    private val imagesRepository: ImagesRepository,
    private val recognizer: Recognizer,
    private val stringsProvider: StringsProvider
) : CoreViewModel<MainState, MainEffect, MainAction>() {

    override val initialState: MainState = MainState()

    init {
        viewModelScope.launch(Dispatchers.Main) { getImages() }
    }

    override suspend fun onAction(action: MainAction) {
        when (action) {
            is RecognizeEmotions -> recognizeEmotions(action.bitmap)
            is Refresh -> getImages()
            is SwipeRefresh -> getImages(isSwipeRefresh = true)
        }
    }

    private suspend fun getImages(isSwipeRefresh: Boolean = false) {
        setState { copy(imagesLoadState = imagesLoadState.loading(isSwipeRefresh = isSwipeRefresh)) }
        imagesRepository.getImages()
            .flowOn(Dispatchers.IO)
            .onEach { setState { copy(imagesLoadState = imagesLoadState.data(it)) } }
            .catch { setState { copy(imagesLoadState = imagesLoadState.error(it as ResponseError)) } }
            .collect()
    }

    private suspend fun recognizeEmotions(bitmap: Bitmap) {
        setState { copy(imagesLoadState = imagesLoadState.loading(isTransparent = true)) }
        recognizer.recognize(bitmap)
            .flatMapConcat { image ->
                imagesRepository.saveDocumentByUrl(image.imageUrl)
                    .map { url -> image to url }
            }
            .flatMapConcat { (image, url) ->
                imagesRepository.saveImage(
                    imageUrl = url,
                    emotions = image.emotions,
                    recognizeTime = image.date,
                    recognizer = image.recognizerType
                )
            }
            .flowOn(Dispatchers.IO)
            .onEach {
                val newList = listOf(it) + state.imagesLoadState.data.orEmpty()
                setState { copy(imagesLoadState = imagesLoadState.data(newList)) }
                OpenImageScreen(image = it).emit()
                ShowMessage(stringsProvider.getString(it.mostPossibleEmotion.asStringResource())).emit()
            }
            .catch {
                val errorMessage = (it as ResponseError).errorMessage
                setState { copy(imagesLoadState = imagesLoadState.data(imagesLoadState.data.orEmpty())) }
                ShowErrorMessage(stringsProvider.getString(errorMessage)).emit()
            }
            .collect()
    }

    private fun saveImage(url: String) {

    }
}