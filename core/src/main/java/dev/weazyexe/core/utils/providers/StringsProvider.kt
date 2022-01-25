package dev.weazyexe.core.utils.providers

import android.content.Context
import androidx.annotation.StringRes

class StringsProvider(private val context: Context) {

    fun string(@StringRes resId: Int): String {
        return context.getString(resId)
    }
}