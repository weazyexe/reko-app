package dev.weazyexe.reko.ui.screen.auth.error

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dev.weazyexe.reko.data.error.UserDoesNotExistException
import dev.weazyexe.reko.ui.common.error.ErrorMapper
import dev.weazyexe.reko.ui.common.error.ResponseError
import dev.weazyexe.reko.ui.common.error.ResponseError.WrongCredentialsError

/**
 * Mapper for auth screen errors
 */
interface AuthErrorMapper : ErrorMapper {

    override fun mapError(exception: Exception): ResponseError =
        when (exception) {
            is FirebaseAuthInvalidUserException,
            is FirebaseAuthInvalidCredentialsException,
            is UserDoesNotExistException -> WrongCredentialsError()
            else -> super.mapError(exception)
        }
}