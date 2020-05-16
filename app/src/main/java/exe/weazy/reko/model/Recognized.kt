package exe.weazy.reko.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Recognized(
    val id: String,
    val image: String,
    val date: Date,
    val recognizer: RecognizerName,
    val emotions: Map<String, Int>
) : Parcelable