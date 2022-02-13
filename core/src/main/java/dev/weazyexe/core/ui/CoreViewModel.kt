package dev.weazyexe.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Base [ViewModel] for all the screens in the app
 */
abstract class CoreViewModel<S : State, E : Effect, A : Action> : ViewModel() {

    /**
     * UI state
     */
    private val _uiState by lazy { MutableStateFlow(initialState) }
    val uiState: StateFlow<S>
        get() = _uiState.asStateFlow()
    protected val state: S
        get() = uiState.value

    /**
     * Side effects to screen channel
     */
    private val _effects = Channel<E>(Channel.BUFFERED)
    val effects: Flow<E>
        get() = _effects.receiveAsFlow()

    /**
     * User's actions channel
     */
    private val actions = Channel<E>(Channel.BUFFERED)

    /**
     * Initial screen state
     */
    protected abstract val initialState: S

    /**
     * Emits some [action]
     */
    fun emit(action: A) {
        viewModelScope.launch(Dispatchers.Main) {
            onAction(action)
        }
    }

    /**
     * Updates the screen state
     */
    protected fun setState(stateBuilder: S.() -> S) {
        _uiState.value = stateBuilder(uiState.value)
    }

    /**
     * Handles new [action]
     */
    protected abstract suspend fun onAction(action: A)

    /**
     * Triggers side-effect
     */
    protected fun E.emit() = viewModelScope.launch {
        _effects.send(this@emit)
    }
}