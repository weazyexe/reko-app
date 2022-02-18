package dev.weazyexe.reko.domain

import androidx.annotation.StringRes
import dev.weazyexe.core.utils.EMPTY_STRING
import dev.weazyexe.reko.R

/**
 * Possible emotions to recognize
 */
enum class Emotion(val type: String) {

    ANGER("ANGER"),
    DISGUST("DISGUST"),
    FEAR("FEAR"),
    HAPPINESS("HAPPINESS"),
    SADNESS("SADNESS"),
    SURPRISE("SURPRISE"),
    NEUTRAL("NEUTRAL"),
    UNKNOWN(EMPTY_STRING);

    @StringRes
    fun asStringResource(): Int =
        when (this) {
            ANGER -> R.string.emotion_anger
            DISGUST -> R.string.emotion_disgust
            FEAR -> R.string.emotion_fear
            HAPPINESS -> R.string.emotion_happiness
            SADNESS -> R.string.emotion_sadness
            SURPRISE -> R.string.emotion_surprise
            NEUTRAL -> R.string.emotion_neutral
            UNKNOWN -> R.string.emotion_unknown
        }

    companion object {

        fun getByType(type: String) = values().firstOrNull { it.type == type } ?: UNKNOWN
    }
}