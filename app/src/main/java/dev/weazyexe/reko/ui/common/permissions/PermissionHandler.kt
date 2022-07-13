package dev.weazyexe.reko.ui.common.permissions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import dev.weazyexe.reko.ui.common.components.dialog.PermissionNotAvailableDialog

/**
 * Base permission handler
 */
@ExperimentalPermissionsApi
@Composable
fun PermissionHandler(
    state: PermissionState,
    isTryingToGetPermission: MutableState<Boolean>,
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    var showPermissionNotAvailableDialog by remember { mutableStateOf(false) }

    when (state.status) {
        is PermissionStatus.Granted -> {
            LaunchedEffect(state) {
                onSuccess()
            }
        }
        is PermissionStatus.Denied -> {
            PermissionNotAvailableDialog(
                shouldShow = showPermissionNotAvailableDialog,
                onSettingsClick = {
                    showPermissionNotAvailableDialog = false
                    isTryingToGetPermission.value = false
                    openSettings(context)
                },
                onCancelClick = {
                    showPermissionNotAvailableDialog = false
                    isTryingToGetPermission.value = false
                }
            )
        }
    }
}

/**
 * Opens app's page in settings app
 */
private fun openSettings(context: Context) {
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
        context.startActivity(this)
    }
}