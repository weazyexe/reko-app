package dev.weazyexe.core.utils.extensions

import android.content.Context
import android.widget.Toast

/**
 * Shows toast alert
 */
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}