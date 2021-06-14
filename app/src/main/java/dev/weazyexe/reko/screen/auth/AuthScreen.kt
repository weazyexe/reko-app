package dev.weazyexe.reko.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.weazyexe.reko.MAIN_SCREEN
import dev.weazyexe.reko.ui.theme.RekoTheme

@Composable
fun AuthScreen(navigateTo: (String) -> Unit) {
    Column {
        Text(text = "auth")
        Button(onClick = { navigateTo(MAIN_SCREEN) }) {
            Text(text = "go to main")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthPreview() {
    RekoTheme {
        AuthScreen {}
    }
}