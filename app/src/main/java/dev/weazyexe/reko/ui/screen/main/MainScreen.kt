package dev.weazyexe.reko.ui.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.weazyexe.reko.ui.theme.RekoTheme

@Composable
fun MainScreen(navigateTo: (String) -> Unit, goBack: () -> Unit) {
    Column {
        Text(text = "main")
        Button(onClick = { goBack() }) {
            Text(text = "back to auth")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    RekoTheme {
        MainScreen({}, {})
    }
}