package exe.weazy.reko.model

import java.util.*

data class Recognized(
    val id: String,
    val image: String,
    val date: Date,
    val emotions: Map<String, Int>
)