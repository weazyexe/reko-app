package dev.weazyexe.reko.ui.common.error

import dev.weazyexe.reko.ui.common.error.ResponseError.*
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

/**
 * Common errors mapper
 */
interface ErrorMapper {

    fun mapError(exception: Exception): ResponseError =
        when (exception) {
            is UnknownHostException,
            is ConnectException -> NoInternetError()
            is TimeoutException -> TimeoutError()
            else -> UnknownError()
        }
}