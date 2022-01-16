package dev.weazyexe.core.utils.extensions

import android.app.Activity
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Перевод [Activity] в режим edge-to-edge
 */
fun Activity.makeEdgeToEdge() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
}

/**
 * Обработка открытия клавиатуры без android:windowSoftInputMode="adjustResize"
 */
fun Activity.adjustResizeViaInsets() {
    val rootView = findViewById<View>(android.R.id.content).rootView
    ViewCompat.setOnApplyWindowInsetsListener(rootView) { _, insets ->
        val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
        rootView.setPadding(0, 0, 0, imeHeight)
        insets
    }
}