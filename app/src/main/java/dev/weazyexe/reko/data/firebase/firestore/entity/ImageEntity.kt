package dev.weazyexe.reko.data.firebase.firestore.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import dev.weazyexe.core.network.Transformable
import dev.weazyexe.core.utils.EMPTY_STRING
import dev.weazyexe.reko.domain.Emotion
import dev.weazyexe.reko.domain.RecognizedImage
import dev.weazyexe.reko.domain.RecognizerType

/**
 * Image entity from Firestore
 */
class ImageEntity(
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String = EMPTY_STRING,

    @get:PropertyName("emotions")
    @set:PropertyName("emotions")
    var emotions: List<EmotionEntity> = emptyList(),

    @get:PropertyName("image")
    @set:PropertyName("image")
    var image: String = EMPTY_STRING,

    @get:PropertyName("recognize_time")
    @set:PropertyName("recognize_time")
    var timestamp: Timestamp = Timestamp.now(),

    @get:PropertyName("recognizer_name")
    @set:PropertyName("recognizer_name")
    var recognizerName: String = EMPTY_STRING
) : Transformable<RecognizedImage> {

    override fun transform(): RecognizedImage =
        RecognizedImage(
            id = id,
            imageUrl = image,
            date = timestamp.toDate(),
            emotions = emotions.associate { it.transform() },
            recognizerType = RecognizerType.getByType(recognizerName)
        )
}

/**
 * Emotion entity from Firestore
 */
class EmotionEntity(
    @get:PropertyName("name")
    @set:PropertyName("name")
    var name: String = EMPTY_STRING,

    @get:PropertyName("value")
    @set:PropertyName("value")
    var value: Int = 0
) : Transformable<Pair<Emotion, Int>> {

    override fun transform(): Pair<Emotion, Int> = Emotion.getByType(name) to value
}