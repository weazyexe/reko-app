package dev.weazyexe.reko.ui.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import dev.weazyexe.core.ui.Route
import dev.weazyexe.reko.ui.theme.RekoTheme

@Composable
fun MainScreen(
    navigateTo: (Route) -> Unit = {},
    goBack: () -> Unit = {}
) {
    Column {
        Text(text = "main")
        Button(onClick = { goBack() }) {
            Text(text = "back to auth")
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun MainPreview() {
    RekoTheme {
        MainScreen()
    }
}