package dev.weazyexe.reko.ui.common.error

import dev.weazyexe.reko.data.error.FacesNotFoundException

/**
 * Mapper for main screen errors
 */
interface MainErrorMapper : ErrorMapper {

    override fun mapError(throwable: Throwable): ResponseError =
        when (throwable) {
            is FacesNotFoundException -> ResponseError.FacesNotFoundError()
            else -> super.mapError(throwable)
        }
}