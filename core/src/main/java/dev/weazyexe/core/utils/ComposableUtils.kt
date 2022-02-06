package dev.weazyexe.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import dev.weazyexe.core.ui.Effect

@Composable
fun <E : Effect?> ReceiveEffect(effect: E, block: E.() -> Unit) {
    LaunchedEffect(effect) {
        block(effect)
    }
}