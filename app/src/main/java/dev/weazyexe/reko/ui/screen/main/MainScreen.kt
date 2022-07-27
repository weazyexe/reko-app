package dev.weazyexe.reko.ui.screen.main

import android.Manifest.permission.CAMERA
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import dev.weazyexe.core.ui.Route
import dev.weazyexe.core.utils.ReceiveEffect
import dev.weazyexe.core.utils.extensions.asBitmap
import dev.weazyexe.reko.ui.common.permissions.PermissionHandler
import dev.weazyexe.reko.ui.screen.main.MainAction.*
import dev.weazyexe.reko.ui.screen.main.MainEffect.ShowErrorMessage
import dev.weazyexe.reko.ui.theme.RekoTheme
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

/**
 * Main screen with feed
 */
@ExperimentalPermissionsApi
@FlowPreview
@Composable
fun MainScreen(
    navigateTo: (Route) -> Unit = {},
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val state by mainViewModel.uiState.collectAsState()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val errorSnackbarHostState = remember { SnackbarHostState() }
    val messageSnackbarHostState = remember { SnackbarHostState() }

    val cameraPermissionState = rememberPermissionState(CAMERA)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            bitmap?.let { mainViewModel.emit(RecognizeEmotions(bitmap)) }
        }
    )

    val getContentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let { mainViewModel.emit(RecognizeEmotions(uri.asBitmap(context))) }
        }
    )

    ReceiveEffect(mainViewModel.effects) {
        when (this) {
            is ShowErrorMessage -> scope.launch {
                errorSnackbarHostState.showSnackbar(message)
            }
            is MainEffect.ShowMessage -> scope.launch {
                messageSnackbarHostState.showSnackbar(message)
            }
            else -> {
                // Do nothing
            }
        }
    }

    val tryToGetCameraPermission = remember { mutableStateOf(false) }
    val tryToGetStoragePermission = remember { mutableStateOf(false) }

    PermissionHandler(
        state = cameraPermissionState,
        isTryingToGetPermission = tryToGetCameraPermission,
        onSuccess = { cameraLauncher.launch() }
    )

    MainBody(
        imagesLoadState = state.imagesLoadState,
        errorSnackbarHostState = errorSnackbarHostState,
        messageSnackbarHostState = messageSnackbarHostState,
        scope = scope,
        onCameraClick = {
            if (cameraPermissionState.status is PermissionStatus.Granted) {
                cameraLauncher.launch()
            } else {
                tryToGetCameraPermission.value = true
                cameraPermissionState.launchPermissionRequest()
            }
        },
        onGalleryClick = { getContentLauncher.launch("image/*") },
        onSwipeRefresh = { mainViewModel.emit(SwipeRefresh) },
        onRetryClick = { mainViewModel.emit(Refresh) },
    )
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun MainPreview() {
    RekoTheme {
        MainBody()
    }
}