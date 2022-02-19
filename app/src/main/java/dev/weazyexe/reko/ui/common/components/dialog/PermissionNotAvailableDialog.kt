package dev.weazyexe.reko.ui.common.components.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoNotDisturb
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.weazyexe.reko.R

/**
 * Dialog for state when permission wasn't allowed by user several times
 */
@Composable
fun PermissionNotAvailableDialog(
    shouldShow: Boolean,
    onSettingsClick: () -> Unit,
    onCancelClick: () -> Unit,
) {
    BaseDialog(
        shouldShow = shouldShow,
        title = stringResource(id = R.string.permissions_denied_title),
        text = stringResource(id = R.string.permissions_denied_with_settings_text),
        confirmButtonText = stringResource(id = R.string.permissions_go_to_settings_text),
        dismissButtonText = stringResource(id = R.string.permissions_cancel_text),
        icon = Icons.Filled.DoNotDisturb,
        onDismissClick = onCancelClick,
        onConfirmClick = onSettingsClick
    )
}