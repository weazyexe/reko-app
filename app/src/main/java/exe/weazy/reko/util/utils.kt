package exe.weazy.reko.util

import android.content.Context
import android.os.Build
import android.util.TypedValue
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.marginBottom
import androidx.core.view.updatePadding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.leinardi.android.speeddial.SpeedDialView
import exe.weazy.reko.util.extensions.updateMargin
import kotlin.random.Random


fun handleBottomInsets(isPadding: Boolean = true, vararg views: View) {
    views.forEach { view ->
        val distance = if (isPadding) view.paddingBottom else view.marginBottom
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            if ((v is FloatingActionButton || v is SpeedDialView) && Build.VERSION_CODES.Q <= Build.VERSION.SDK_INT) {
                val tappable = insets.tappableElementInsets
                v.updateMargin(
                    bottom = tappable.bottom + v.marginBottom
                )
            } else {
                if (isPadding) {
                    v.updatePadding(bottom = distance + insets.systemWindowInsetBottom)
                } else {
                    v.updateMargin(bottom = distance + insets.systemWindowInsetBottom)
                }
            }

            insets
        }
    }
}

fun handleTopInsets(vararg views: View) {
    views.forEach { view ->
        val padding = view.paddingTop
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            v.updatePadding(
                top = padding + insets.systemWindowInsetTop
            )
            insets
        }
    }
}

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