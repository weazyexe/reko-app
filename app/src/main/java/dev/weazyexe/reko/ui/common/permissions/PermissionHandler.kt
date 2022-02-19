package dev.weazyexe.reko.ui.common.permissions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.PermissionState
import dev.weazyexe.reko.ui.common.components.dialog.PermissionNotAvailableDialog
import dev.weazyexe.reko.ui.common.components.dialog.PermissionNotGrantedDialog

/**
 * Base permission handler
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionHandler(
    state: PermissionState,
    isTryingToGetPermission: MutableState<Boolean>,
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    var showPermissionNotGrantedDialog by remember { mutableStateOf(false) }
    var showPermissionNotAvailableDialog by remember { mutableStateOf(false) }

    if (state.permissionRequested && isTryingToGetPermission.value) {
        PermissionRequired(
            permissionState = state,
            permissionNotGrantedContent = {
                showPermissionNotGrantedDialog = true
                PermissionNotGrantedDialog(
                    shouldShow = showPermissionNotGrantedDialog,
                    onDismissClick = {
                        showPermissionNotGrantedDialog = false
                        isTryingToGetPermission.value = false
                    }
                )
            },
            permissionNotAvailableContent = {
                showPermissionNotAvailableDialog = true
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
            },
            content = {
                LaunchedEffect(state) {
                    onSuccess()
                }
            }
        )
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