package dev.weazyexe.reko.ui.common.error

import androidx.annotation.StringRes
import dev.weazyexe.reko.R

/**
 * Exception wrapper
 */
sealed class ResponseError(@StringRes open val errorMessage: Int): Throwable() {

    class UnknownError(
        override val errorMessage: Int = R.string.error_unknown
    ) : ResponseError(errorMessage)

    class NoInternetError(
        override val errorMessage: Int = R.string.error_no_internet
    ) : ResponseError(errorMessage)

    class TimeoutError(
        override val errorMessage: Int = R.string.error_timed_out
    ) : ResponseError(errorMessage)

    class WrongCredentialsError(
        override val errorMessage: Int = R.string.error_user_does_not_exist
    ) : ResponseError(errorMessage)

    class TooManyRequestsError(
        override val errorMessage: Int = R.string.error_too_many_requests
    ) : ResponseError(errorMessage)
}