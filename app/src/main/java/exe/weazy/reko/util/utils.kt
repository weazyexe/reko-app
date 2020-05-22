package exe.weazy.reko.util

import android.content.Context
import android.util.TypedValue
import kotlin.random.Random

fun generateId(length: Int = 10): String {
    val symbols = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
    val sb = StringBuilder()
    for (i in 0..length) {
        sb.append(symbols[Random.nextInt(symbols.length)])
    }
    return sb.toString()
}

fun getDefaultColor(context: Context, attrId: Int): Int {
    val value = TypedValue()
    context.theme.resolveAttribute(attrId, value, true)
    return value.data
}

fun getEmotion(index: Int): String {
    val map = mapOf(
        0 to "ANGER",
        1 to "DISGUST",
        2 to "FEAR",
        3 to "HAPPINESS",
        4 to "SAD",
        5 to "SURPRISE",
        6 to "NEUTRAL"
    )
    return map[index] ?: "NO EMOTION"
}