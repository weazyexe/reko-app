package dev.weazyexe.reko.ui.screen.main

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import dev.weazyexe.core.ui.Route
import dev.weazyexe.core.utils.ReceiveEffect
import dev.weazyexe.reko.ui.common.permissions.PermissionHandler
import dev.weazyexe.reko.ui.theme.RekoTheme

/**
 * Main screen with feed
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    navigateTo: (Route) -> Unit = {},
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val state by mainViewModel.uiState.collectAsState()

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) {
        // handle bitmap
    }
    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )

    ReceiveEffect(mainViewModel.effects) {
        // TODO handle effects
    }

    val tryToGetPermission = remember { mutableStateOf(false) }
    PermissionHandler(
        state = cameraPermissionState,
        isTryingToGetPermission = tryToGetPermission,
        onSuccess = { cameraLauncher.launch() }
    )

    MainBody(
        imagesLoadState = state.imagesLoadState,
        onCameraClick = {
            tryToGetPermission.value = true
            when {
                cameraPermissionState.hasPermission -> {
                    cameraLauncher.launch()
                }
                !cameraPermissionState.permissionRequested -> {
                    cameraPermissionState.launchPermissionRequest()
                }
                else -> {
                    tryToGetPermission.value = true
                }
            }
        }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun MainPreview() {
    RekoTheme {
        MainBody()
    }
}