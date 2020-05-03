package exe.weazy.reko.util

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.marginBottom
import androidx.core.view.updatePadding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import exe.weazy.reko.model.Meme
import exe.weazy.reko.util.extensions.updateMargin

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

fun share(activity: Activity, meme: Meme) {

    val text = "${meme.title}\n${meme.description}\n${meme.photoUrl}"

    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    activity.startActivity(shareIntent)
}