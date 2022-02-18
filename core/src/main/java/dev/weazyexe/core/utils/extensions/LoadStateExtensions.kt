package dev.weazyexe.core.utils.extensions

import dev.weazyexe.core.ui.LoadState

/**
 * Transforms current [LoadState] to loading
 */
fun <T> LoadState<T>.loading(isSwipeRefresh: Boolean = false) =
    LoadState.loading(
        isSwipeRefresh = isSwipeRefresh,
        oldData = data
    )

/**
 * Transforms current [LoadState] to success with [data]
 */
fun <T> LoadState<T>.data(data: T): LoadState<T> = LoadState.data(data)

/**
 * Transforms current [LoadState] to success with empty data
 */
fun LoadState<Unit>.data(): LoadState<Unit> = LoadState.data()

/**
 * Transforms current [LoadState] to error state with [throwable] error
 */
fun <T> LoadState<T>.error(throwable: Throwable) = LoadState.error(throwable, data)