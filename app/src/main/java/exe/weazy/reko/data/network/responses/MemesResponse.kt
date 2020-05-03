package exe.weazy.reko.data.network.responses

import com.google.gson.annotations.SerializedName
import exe.weazy.reko.model.Meme
import java.util.*

data class MemesResponse(
    @SerializedName("id")
    val id: Long,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("isFavorite")
    val isFavorite: Boolean,

    @SerializedName("createdDate")
    val createDate: Long,

    @SerializedName("photoUrl")
    val photoUrl: String
) {
    fun convert() = Meme(
        id = id,
        title = title,
        description = description,
        isFavorite = isFavorite,
        createDate = Date(createDate),
        photoUrl = photoUrl
    )
}