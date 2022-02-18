package dev.weazyexe.reko.data.network.response

import dev.weazyexe.core.network.Transformable
import dev.weazyexe.reko.domain.Emotion
import dev.weazyexe.reko.domain.RecognizedImage
import dev.weazyexe.reko.domain.Recognizer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

/**
 * Recognized image entity from SkyBiometry service
 */
@Serializable
data class RecognizedImageEntity(
    val status: String,
    val photos: List<PhotoEntity>
) : Transformable<RecognizedImage> {

    override fun transform(): RecognizedImage {
        val photo = photos.first()
        val tag = photo.tags.first()

        return RecognizedImage(
            id = photo.pid.substring(0..7),
            imageUrl = photo.url.replace("http://", "https://"),
            date = Date(),
            emotions = getEmotions(tag),
            recognizer = Recognizer.SKY_BIOMETRY
        )
    }

    private fun getEmotions(tag: TagEntity): Map<Emotion, Int> {
        val emotions = mutableMapOf<Emotion, Int>()

        if (tag.attributes.neutral.confidence > 0) {
            emotions[Emotion.NEUTRAL] = tag.attributes.neutral.confidence
        }

        if (tag.attributes.happiness.confidence > 0) {
            emotions[Emotion.HAPPINESS] = tag.attributes.happiness.confidence
        }

        if (tag.attributes.sadness.confidence > 0) {
            emotions[Emotion.SADNESS] = tag.attributes.sadness.confidence
        }

        if (tag.attributes.disgust.confidence > 0) {
            emotions[Emotion.DISGUST] = tag.attributes.disgust.confidence
        }

        if (tag.attributes.surprise.confidence > 0) {
            emotions[Emotion.SURPRISE] = tag.attributes.surprise.confidence
        }

        if (tag.attributes.anger.confidence > 0) {
            emotions[Emotion.ANGER] = tag.attributes.anger.confidence
        }

        if (tag.attributes.fear.confidence > 0) {
            emotions[Emotion.FEAR] = tag.attributes.fear.confidence
        }

        return emotions
    }
}

/**
 * Photo res
 */
@Serializable
data class PhotoEntity(
    val pid: String,
    val url: String,
    val tags: List<TagEntity>
)

@Serializable
data class TagEntity(
    val attributes: AttributesEntity
)

@Serializable
data class AttributesEntity(
    @SerialName("neutral_mood")
    val neutral: EmotionEntity,
    val anger: EmotionEntity,
    val disgust: EmotionEntity,
    val fear: EmotionEntity,
    val happiness: EmotionEntity,
    val sadness: EmotionEntity,
    val surprise: EmotionEntity
)

@Serializable
data class EmotionEntity(
    val value: String,
    val confidence: Int
)