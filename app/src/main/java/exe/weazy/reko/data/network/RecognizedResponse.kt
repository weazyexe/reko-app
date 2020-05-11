package exe.weazy.reko.data.network

import com.google.gson.annotations.SerializedName
import exe.weazy.reko.model.Recognized
import java.util.*

data class RecognizedResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("photos")
    val photos: List<PhotoResponse>
) {
    fun convert(): Recognized {
        val photo = photos.first()
        val tag = photo.tags.first()

        return Recognized(
            id = photo.id,
            image = photo.url,
            date = Date(),
            emotions = getEmotions(tag)
        )
    }

    private fun getEmotions(tag: TagResponse): Map<String, Int> {
        val emotions = mutableMapOf<String, Int>()

        if (tag.attributes.neutral.value) {
            emotions["NEUTRAL"] = tag.attributes.neutral.confidence
        }

        if (tag.attributes.happiness.value) {
            emotions["HAPPINESS"] = tag.attributes.happiness.confidence
        }

        if (tag.attributes.sadness.value) {
            emotions["SADNESS"] = tag.attributes.sadness.confidence
        }

        if (tag.attributes.disgust.value) {
            emotions["DISGUST"] = tag.attributes.disgust.confidence
        }

        if (tag.attributes.surprise.value) {
            emotions["SURPRISE"] = tag.attributes.surprise.confidence
        }

        if (tag.attributes.anger.value) {
            emotions["ANGER"] = tag.attributes.anger.confidence
        }

        if (tag.attributes.fear.value) {
            emotions["FEAR"] = tag.attributes.fear.confidence
        }

        return emotions
    }
}

data class PhotoResponse(
    @SerializedName("pid")
    val id: String,

    @SerializedName("url")
    val url: String,

    @SerializedName("tags")
    val tags: List<TagResponse>
)

data class TagResponse(
    @SerializedName("attributes")
    val attributes: AttributesResponse
)

data class AttributesResponse(
    @SerializedName("neutral_mood")
    val neutral: EmotionResponse,

    @SerializedName("anger")
    val anger: EmotionResponse,

    @SerializedName("disgust")
    val disgust: EmotionResponse,

    @SerializedName("fear")
    val fear: EmotionResponse,

    @SerializedName("happiness")
    val happiness: EmotionResponse,

    @SerializedName("sadness")
    val sadness: EmotionResponse,

    @SerializedName("surprise")
    val surprise: EmotionResponse
)

data class EmotionResponse(
    @SerializedName("value")
    val value: Boolean,

    @SerializedName("confidence")
    val confidence: Int
)