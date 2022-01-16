package dev.weazyexe.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import dev.weazyexe.core.ui.Effect

@Composable
fun <E : Effect> ReceiveEffect(effectState: State<E>, block: E.() -> Unit) {
    LaunchedEffect(effectState.value) {
        block(effectState.value)
    }
}