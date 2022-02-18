package dev.weazyexe.reko.data.firebase.firestore.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import dev.weazyexe.core.network.Transformable
import dev.weazyexe.reko.domain.Emotion
import dev.weazyexe.reko.domain.RecognizedImage
import dev.weazyexe.reko.domain.Recognizer

/**
 * Image entity from Firestore
 */
data class ImageEntity(
    @PropertyName("id") val id: String,
    @PropertyName("emotions") val emotions: List<EmotionEntity>,
    @PropertyName("image") val image: String,
    @PropertyName("recognize_time") val timestamp: Timestamp,
    @PropertyName("recognizer_name") val recognizerName: String
) : Transformable<RecognizedImage> {

    override fun transform(): RecognizedImage =
        RecognizedImage(
            id = id,
            imageUrl = image,
            date = timestamp.toDate(),
            emotions = emotions.associate { it.transform() },
            recognizer = Recognizer.getByType(recognizerName)
        )
}

/**
 * Emotion entity from Firestore
 */
data class EmotionEntity(
    @PropertyName("name") val name: String,
    @PropertyName("value") val value: Int
) : Transformable<Pair<Emotion, Int>> {

    override fun transform(): Pair<Emotion, Int> = Emotion.getByType(name) to value
}