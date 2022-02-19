package dev.weazyexe.reko.ui.common.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Base material alert dialog
 */
@Composable
fun BaseDialog(
    shouldShow: Boolean,
    title: String,
    text: String,
    confirmButtonText: String? = null,
    dismissButtonText: String,
    icon: ImageVector?,
    onConfirmClick: (() -> Unit)? = null,
    onDismissClick: () -> Unit
) {
    if (shouldShow) {
        AlertDialog(
            onDismissRequest = { onDismissClick() },
            title = { Text(text = title) },
            text = { Text(text = text) },
            confirmButton = {
                if (confirmButtonText != null) {
                    TextButton(
                        onClick = { onConfirmClick?.invoke() }
                    ) {
                        Text(text = confirmButtonText)
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onDismissClick() }
                ) {
                    Text(text = dismissButtonText)
                }
            },
            icon = {
                if (icon != null) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            }
        )
    }
}