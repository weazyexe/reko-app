package dev.weazyexe.reko.ui.common.components.snackbar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.weazyexe.reko.R

/**
 * Base themed snackbar with icon
 */
@Composable
fun BaseSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    icon: ImageVector? = null
) {
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier
    ) {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            containerColor = containerColor,
            contentColor = contentColor
        ) {
            Row {
                icon?.let {
                    Icon(
                        modifier = Modifier.padding(end = 8.dp),
                        imageVector = icon,
                        contentDescription = stringResource(
                            id = R.string.content_description_snackbar_icon
                        )
                    )
                }
                Text(
                    text = it.visuals.message,
                    modifier = Modifier.align(Alignment.CenterVertically),
                )
            }
        }
    }
}