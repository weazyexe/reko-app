package dev.weazyexe.reko.ui.common.components.snackbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Default snackbar implementation
 */
@Composable
fun DefaultSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) = BaseSnackbar(
    modifier = modifier,
    snackbarHostState = snackbarHostState,
    icon = Icons.Filled.Info
)