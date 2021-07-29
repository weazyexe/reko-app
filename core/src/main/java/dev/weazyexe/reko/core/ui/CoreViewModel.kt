package dev.weazyexe.reko.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class CoreViewModel : ViewModel() {

    private val _state by lazy {
        MutableStateFlow(initialState)
    }
    val state: StateFlow<ViewState>
        get() = _state.asStateFlow()


    protected abstract val initialState: ViewState

    fun update(newState: ViewState) {
        viewModelScope.launch {
            _state.emit(newState)
        }
    }
}