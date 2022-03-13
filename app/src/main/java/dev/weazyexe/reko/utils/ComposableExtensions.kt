package dev.weazyexe.reko.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

/**
 * Clickable [Modifier] but without a ripple effect
 */
@Composable
fun Modifier.noRippleClickable(onClick: () -> Unit = {}): Modifier =
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
        onClick = onClick
    )