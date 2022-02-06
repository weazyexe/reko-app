package dev.weazyexe.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Базовая [ViewModel] для всех экранов приложения
 */
abstract class CoreViewModel<S : State, E : Effect, A : Action> : ViewModel() {

    private val _uiState by lazy { MutableStateFlow(initialState) }
    val uiState: StateFlow<S>
        get() = _uiState.asStateFlow()
    protected val state: S
        get() = uiState.value

    private val _effects = Channel<E?>(Channel.BUFFERED)
    val effects: Flow<E?>
        get() = _effects.receiveAsFlow()

    private val actions = Channel<E>(Channel.BUFFERED)

    /**
     * Первоначальное состояние экрана
     */
    protected abstract val initialState: S

    fun emit(action: A) {
        viewModelScope.launch(Dispatchers.Main) {
            handleAction(action)
        }
    }

    abstract suspend fun handleAction(action: A)

    protected suspend fun <T> query(
        query: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        onError: suspend (Exception) -> Unit
    ) = viewModelScope.launch(Dispatchers.Main) {
        try {
            val result = withContext(Dispatchers.IO) { query() }
            onSuccess(result)
        } catch (e: Exception) {
            onError(e)
        }
    }

    /**
     * Триггер сайд-эффекта
     */
    protected fun E.emit() = viewModelScope.launch {
        _effects.send(this@emit)
    }

    /**
     * Вызов обновления состояния экрана
     * и сохранение состояния
     */
    protected suspend fun S.emit() {
        _uiState.emit(this)
    }
}