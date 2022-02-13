package dev.weazyexe.core.ui

import java.io.Serializable

/**
 * Loading state for data
 *
 * @property data loaded data
 * @property error error what was thrown during the loading
 * @property isLoading loading state
 * @property isSwipeRefresh is loading was launched from swipe refresh
 */
data class LoadState<T>(
    val data: T? = null,
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val isSwipeRefresh: Boolean = false
) : Serializable {

    companion object {

        /**
         * Create loading [LoadState]
         */
        fun <T> loading(
            isSwipeRefresh: Boolean = false,
            oldData: T? = null
        ): LoadState<T> =
            LoadState(
                data = oldData,
                error = null,
                isLoading = true,
                isSwipeRefresh = isSwipeRefresh
            )

        /**
         * Create error [LoadState]
         */
        fun <T> error(
            e: Throwable,
            oldData: T? = null
        ): LoadState<T> =
            LoadState(
                data = oldData,
                error = e,
                isLoading = false,
                isSwipeRefresh = false
            )

        /**
         * Create successful [LoadState] with data
         */
        fun <T> data(data: T): LoadState<T> =
            LoadState(
                data = data,
                error = null,
                isLoading = false,
                isSwipeRefresh = false
            )

        /**
         * Create successful [LoadState] with [Unit]
         */
        fun data(): LoadState<Unit> =
            LoadState(
                data = Unit,
                error = null,
                isLoading = false,
                isSwipeRefresh = false
            )
    }
}