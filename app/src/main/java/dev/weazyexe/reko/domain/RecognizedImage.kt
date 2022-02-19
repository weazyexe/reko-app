package dev.weazyexe.reko.domain

import dev.weazyexe.core.utils.EMPTY_STRING
import java.util.*

/**
 * Recognized image
 *
 * @param id identifier of image
 * @param imageUrl url for image to recognize
 * @param date date of image processing
 * @param emotions map of emotions with probabilities
 * @param recognizerType which recognizer was used to recognize the [emotions]
 */
data class RecognizedImage(
    val id: String = UUID.randomUUID().toString(),
    val imageUrl: String = EMPTY_STRING,
    val date: Date = Date(),
    val emotions: Map<Emotion, Int> = emptyMap(),
    val recognizerType: RecognizerType = RecognizerType.LOCAL
) {

    val mostPossibleEmotion: Emotion
        get() = emotions.maxByOrNull { it.value }?.key ?: Emotion.UNKNOWN
}