package dev.weazyexe.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import dev.weazyexe.core.ui.Effect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

/**
 * Receive side effects from [effects] flow
 */
@Composable
fun <E : Effect> ReceiveEffect(effects: Flow<E>, block: E.() -> Unit) {
    LaunchedEffect(Unit) {
        effects.onEach {
            block(it)
        }.collect()
    }
}