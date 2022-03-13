package dev.weazyexe.reko.data.firebase.firestore.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import dev.weazyexe.core.network.Transformable
import dev.weazyexe.core.utils.EMPTY_STRING
import dev.weazyexe.reko.data.firebase.common.FirebaseEntity
import dev.weazyexe.reko.data.firebase.common.asMap
import dev.weazyexe.reko.domain.Emotion
import dev.weazyexe.reko.domain.RecognizedImage
import dev.weazyexe.reko.domain.RecognizerType

/**
 * Image entity from Firestore
 */
data class ImageEntity(
    @PropertyName("id") val id: String = EMPTY_STRING,
    @PropertyName("emotions") val emotions: List<EmotionEntity> = emptyList(),
    @PropertyName("image") val image: String = EMPTY_STRING,
    @PropertyName("recognize_time") val timestamp: Timestamp = Timestamp.now(),
    @PropertyName("recognizer_name") val recognizerName: String = EMPTY_STRING
) : Transformable<RecognizedImage>, FirebaseEntity {

    override fun transform(): RecognizedImage =
        RecognizedImage(
            id = id,
            imageUrl = image,
            date = timestamp.toDate(),
            emotions = emotions.associate { it.transform() },
            recognizerType = RecognizerType.getByType(recognizerName)
        )

    override fun asMap(): Map<String, Any?> = hashMapOf(
        "id" to id,
        "emotions" to emotions.asMap(),
        "image" to image,
        "recognize_time" to timestamp,
        "recognizer_name" to recognizerName
    )
}

/**
 * Emotion entity from Firestore
 */
data class EmotionEntity(
    @PropertyName("name") val name: String = EMPTY_STRING,
    @PropertyName("value") val value: Int = 0
) : Transformable<Pair<Emotion, Int>>, FirebaseEntity {

    override fun transform(): Pair<Emotion, Int> = Emotion.getByType(name) to value

    override fun asMap(): Map<String, Any?> = hashMapOf("name" to name, "value" to value)
}