package exe.weazy.reko.util.extensions

import android.app.Activity
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import com.google.android.material.snackbar.Snackbar
import exe.weazy.reko.R


fun Activity.showErrorSnackbar(stringRes: Int, view: View) {
    val snackbar = Snackbar.make(view, stringRes, Snackbar.LENGTH_LONG)
    snackbar.setBackgroundTint(getColor(R.color.colorRed))
    snackbar.setTextColor(getColor(R.color.colorWhite))
    snackbar.show()
}

fun View.updateMargin(left: Int? = null, right: Int? = null, top: Int? = null, bottom: Int? = null) {
    val params = layoutParams
    if (params is MarginLayoutParams) {
        params.setMargins(left ?: marginLeft, top ?: marginTop,
            right ?: marginRight, bottom ?: marginBottom)
        layoutParams = params
        requestLayout()
    }
}