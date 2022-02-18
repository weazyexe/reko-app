package dev.weazyexe.reko.ui.screen.main

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.weazyexe.core.ui.CoreViewModel
import dev.weazyexe.core.utils.extensions.data
import dev.weazyexe.core.utils.extensions.error
import dev.weazyexe.core.utils.extensions.loading
import dev.weazyexe.reko.data.repository.ImagesRepository
import dev.weazyexe.reko.ui.common.error.AuthErrorMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for [MainScreen]
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val imagesRepository: ImagesRepository
) : CoreViewModel<MainState, MainEffect, MainAction>(), AuthErrorMapper {

    override val initialState: MainState = MainState()

    init {
        viewModelScope.launch(Dispatchers.Main) { getImages() }
    }

    override suspend fun onAction(action: MainAction) {
        // Do nothing
    }

    private suspend fun getImages(isSwipeRefresh: Boolean = false) {
        setState { copy(imagesLoadState = imagesLoadState.loading(isSwipeRefresh)) }
        imagesRepository.getImages()
            .flowOn(Dispatchers.IO)
            .onEach { setState { copy(imagesLoadState = imagesLoadState.data(it)) } }
            .catch { setState { copy(imagesLoadState = imagesLoadState.error(mapError(it))) } }
            .collect()
    }
}