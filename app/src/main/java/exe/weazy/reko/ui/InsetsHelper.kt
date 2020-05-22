package exe.weazy.reko.ui

import android.os.Build
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.core.view.updatePadding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.leinardi.android.speeddial.SpeedDialView
import exe.weazy.reko.util.extensions.updateMargin

object InsetsHelper {
    fun handleBottom(isPadding: Boolean, vararg views: View) {
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

    fun handleTop(isPadding: Boolean, vararg views: View) {
        views.forEach { view ->
            val distance = if (isPadding) view.paddingTop else view.marginTop
            ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
                if (isPadding) {
                    v.updatePadding(
                        top = distance + insets.systemWindowInsetTop
                    )
                } else {
                    v.updateMargin(
                        top = distance + insets.systemWindowInsetTop
                    )
                }

                insets
            }
        }
    }
}