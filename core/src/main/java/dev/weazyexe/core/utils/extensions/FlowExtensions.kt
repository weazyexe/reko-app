package dev.weazyexe.core.utils.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * Обработка ошибок в [Flow]
 */
fun <T> Flow<T>.handleErrors(
    handleBlock: suspend (Exception) -> Unit
): Flow<T> = flow {
    try {
        map { it }
    } catch (e: Exception) {
        handleBlock(e)
    }
}