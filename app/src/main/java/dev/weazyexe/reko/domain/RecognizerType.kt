package dev.weazyexe.reko.domain

import androidx.annotation.StringRes
import dev.weazyexe.reko.R
import dev.weazyexe.reko.domain.RecognizerType.LOCAL
import dev.weazyexe.reko.domain.RecognizerType.SKY_BIOMETRY

/**
 * Types of emotion recognizers
 *
 * @property LOCAL recognizer with local trained model
 * @property SKY_BIOMETRY recognizer from SkyBiometry API
 */
enum class RecognizerType(val type: String) {

    LOCAL("LOCAL"),
    SKY_BIOMETRY("SKY_BIOMETRY"),
    UNKNOWN("UNKNOWN");

    @StringRes
    fun asStringResource(): Int =
        when (this) {
            LOCAL -> R.string.recognizer_local
            SKY_BIOMETRY -> R.string.recognizer_sky_biometry
            UNKNOWN -> R.string.recognizer_unknown
        }

    companion object {

        fun getByType(type: String) = values()
            .firstOrNull { it.type == type } ?: UNKNOWN
    }
}