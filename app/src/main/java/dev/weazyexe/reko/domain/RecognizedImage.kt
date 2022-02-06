package dev.weazyexe.reko.domain

import java.util.*

/**
 * Recognized image
 *
 * @param id identifier of image
 * @param imageUrl url for image to recognize
 * @param date date of image processing
 * @param emotions map of emotions with probabilities
 */
data class RecognizedImage(
    val id: String,
    val imageUrl: String,
    val date: Date,
    val emotions: Map<Emotion, Int>
)