package dev.weazyexe.core.ui

import java.io.Serializable
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * Базовое состояние загрузки данных
 *
 * @property data загруженные данные
 * @property error [Exception], брошенная при загрузке
 * @property isLoading состояние загрузки
 * @property isSwipeRefresh состояние загрузки через свайп
 */
data class LoadState<T>(
    val data: T? = null,
    val error: Exception? = null,
    val isLoading: Boolean = false,
    val isSwipeRefresh: Boolean = false
) : Serializable {

    companion object {

        /**
         * Создать [LoadState] в состоянии загрузки
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
         * Создать [LoadState] в состоянии ошибки
         */
        fun <T> error(
            e: Exception,
            oldData: T? = null
        ): LoadState<T> =
            LoadState(
                data = oldData,
                error = e,
                isLoading = false,
                isSwipeRefresh = false
            )

        /**
         * Создать [LoadState] в состоянии показа данных
         */
        fun <T> data(data: T): LoadState<T> =
            LoadState(
                data = data,
                error = null,
                isLoading = false,
                isSwipeRefresh = false
            )
    }

    fun isNetworkError(): Boolean = error is ConnectException || error is UnknownHostException
}