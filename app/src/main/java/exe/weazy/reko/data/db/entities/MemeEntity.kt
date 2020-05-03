package exe.weazy.reko.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import exe.weazy.reko.model.Meme
import java.util.*

@Entity(tableName = "memes")
data class MemeEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "isFavorite")
    val isFavorite: Boolean,

    @ColumnInfo(name = "createDate")
    val createDate: Long,

    @ColumnInfo(name = "photoUrl")
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