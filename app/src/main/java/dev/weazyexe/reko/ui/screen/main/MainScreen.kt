package dev.weazyexe.reko.ui.screen.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import dev.weazyexe.core.ui.Route
import dev.weazyexe.core.utils.ReceiveEffect
import dev.weazyexe.reko.ui.theme.RekoTheme

/**
 * Main screen with feed
 */
@Composable
fun MainScreen(
    navigateTo: (Route) -> Unit = {},
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val state by mainViewModel.uiState.collectAsState()

    ReceiveEffect(mainViewModel.effects) {
        // TODO handle effects
    }

    MainBody(
        imagesLoadState = state.imagesLoadState
    )
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun MainPreview() {
    RekoTheme {
        MainBody()
    }
}