package dev.weazyexe.core.utils.extensions

import android.app.Activity
import androidx.core.view.WindowCompat

/**
 * Перевод [Activity] в режим edge-to-edge
 */
fun Activity.makeEdgeToEdge() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
}