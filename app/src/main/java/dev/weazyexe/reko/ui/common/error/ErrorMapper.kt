package dev.weazyexe.reko.ui.common.error

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import dev.weazyexe.reko.ui.common.error.ResponseError.*
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

/**
 * Common errors mapper
 */
interface ErrorMapper {

    fun mapError(throwable: Throwable): ResponseError =
        when (throwable) {
            is UnknownHostException,
            is ConnectException,
            is FirebaseNetworkException -> NoInternetError()
            is TimeoutException -> TimeoutError()
            is FirebaseTooManyRequestsException -> TooManyRequestsError()
            else -> UnknownError()
        }
}