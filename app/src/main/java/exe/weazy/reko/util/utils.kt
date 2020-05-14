package exe.weazy.reko.util

import android.os.Build
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.marginBottom
import androidx.core.view.updatePadding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import exe.weazy.reko.util.extensions.updateMargin
import kotlin.random.Random


fun handleBottomInsets(vararg views: View) {
    views.forEach { view ->
        val padding = view.paddingBottom
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            if (v is FloatingActionButton && Build.VERSION_CODES.Q <= Build.VERSION.SDK_INT) {
                val tappable = insets.tappableElementInsets
                v.updateMargin(
                    bottom = tappable.bottom + v.marginBottom
                )
            } else {
                v.updatePadding(bottom = padding + insets.systemWindowInsetBottom)
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