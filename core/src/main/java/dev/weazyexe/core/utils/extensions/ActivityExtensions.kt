package dev.weazyexe.core.utils.extensions

import android.app.Activity
import androidx.core.view.WindowCompat

/**
 * Enable edge-to-edge mode for [Activity]
 */
fun Activity.makeEdgeToEdge() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
}