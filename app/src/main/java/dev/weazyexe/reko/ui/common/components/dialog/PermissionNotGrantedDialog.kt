package dev.weazyexe.reko.ui.common.components.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoNotDisturb
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.weazyexe.reko.R

/**
 * Dialog for state when permission wasn't allowed by user once
 */
@Composable
fun PermissionNotGrantedDialog(
    shouldShow: Boolean,
    onDismissClick: () -> Unit
) {
    BaseDialog(
        shouldShow = shouldShow,
        title = stringResource(id = R.string.permissions_denied_title),
        text = stringResource(id = R.string.permissions_denied_text),
        dismissButtonText = stringResource(id = R.string.permissions_ok_text),
        icon = Icons.Filled.DoNotDisturb,
        onDismissClick = onDismissClick
    )
}