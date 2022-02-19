package dev.weazyexe.reko.recognizer

import android.graphics.Bitmap
import dev.weazyexe.reko.domain.RecognizedImage
import kotlinx.coroutines.flow.Flow

/**
 * Base recognizer entity interface
 */
interface Recognizer {

    /**
     * Recognize emotions from image from [imageUri]
     */
    fun recognize(bitmap: Bitmap): Flow<RecognizedImage>
}