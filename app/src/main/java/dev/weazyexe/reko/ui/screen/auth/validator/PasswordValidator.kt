package dev.weazyexe.reko.ui.screen.auth.validator

import dagger.hilt.android.scopes.ViewModelScoped
import dev.weazyexe.core.utils.providers.StringsProvider
import dev.weazyexe.reko.R
import dev.weazyexe.reko.ui.common.validator.Validator
import javax.inject.Inject

/**
 * Password validator
 */
@ViewModelScoped
class PasswordValidator @Inject constructor(
    private val stringsProvider: StringsProvider
) : Validator<String> {

    override fun validate(data: String): String? =
        when {
            data.isEmpty() -> stringsProvider.getString(R.string.auth_password_is_empty_error_text)
            else -> null
        }

}