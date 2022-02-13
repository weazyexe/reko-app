package dev.weazyexe.core.utils.providers

import android.content.Context
import androidx.annotation.StringRes

/**
 * Provider for strings from resources.
 * Usually can be used from view models
 *
 * @param context application context
 */
class StringsProvider(private val context: Context) {

    /**
     * Gets string with [resId] from resources
     */
    fun getString(@StringRes resId: Int): String {
        return context.getString(resId)
    }
}