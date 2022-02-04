package dev.weazyexe.reko.ui.common.error

import androidx.annotation.StringRes
import dev.weazyexe.reko.R

/**
 * Exception wrapper
 */
sealed class ResponseError(@StringRes open val message: Int) {

    class UnknownError(
        override val message: Int = R.string.error_unknown
    ) : ResponseError(message)

    class NoInternetError(
        override val message: Int = R.string.error_no_internet
    ) : ResponseError(message)

    class TimeoutError(
        override val message: Int = R.string.error_timed_out
    ) : ResponseError(message)

    class WrongCredentialsError(
        override val message: Int = R.string.error_user_does_not_exist
    ) : ResponseError(message)
}