package dev.weazyexe.reko.utils

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dev.weazyexe.reko.data.error.FacesNotFoundException
import dev.weazyexe.reko.data.error.FirebaseDocumentUploadException
import dev.weazyexe.reko.data.error.SessionInvalidException
import dev.weazyexe.reko.data.error.UserDoesNotExistException
import dev.weazyexe.reko.ui.common.error.ResponseError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

/**
 * Flow builder on IO thread with error mapping
 */
fun <T> flowIo(action: suspend FlowCollector<T>.() -> T) =
    flow { emit(action(this)) }
        .flowOn(Dispatchers.IO)
        .catch {
            throw when (it) {
                is UnknownHostException,
                is ConnectException,
                is FirebaseNetworkException -> ResponseError.NoInternetError()

                is FacesNotFoundException -> ResponseError.FacesNotFoundError()
                is SessionInvalidException -> ResponseError.SessionInvalidError()
                is FirebaseDocumentUploadException -> ResponseError.DocumentUploadError()

                is FirebaseAuthInvalidUserException,
                is FirebaseAuthInvalidCredentialsException,
                is UserDoesNotExistException -> ResponseError.WrongCredentialsError()

                is TimeoutException -> ResponseError.TimeoutError()

                is FirebaseTooManyRequestsException -> ResponseError.TooManyRequestsError()

                else -> {
                    Log.e("REKO", "Unknown error", it)
                    UnknownError()
                }
            }
        }