package dev.weazyexe.reko.ui.common.components.snackbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Error snackbar implementation
 */
@Composable
fun ErrorSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) = BaseSnackbar(
    modifier = modifier,
    snackbarHostState = snackbarHostState,
    contentColor = MaterialTheme.colorScheme.onErrorContainer,
    containerColor = MaterialTheme.colorScheme.errorContainer,
    icon = Icons.Filled.Error
)