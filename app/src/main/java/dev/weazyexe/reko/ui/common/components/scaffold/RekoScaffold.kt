package dev.weazyexe.reko.ui.common.components.scaffold

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.weazyexe.core.ui.LoadState
import dev.weazyexe.reko.R
import dev.weazyexe.reko.ui.common.error.ResponseError
import dev.weazyexe.reko.ui.theme.RekoTheme

/**
 * Base scaffold for app. It includes all the [LoadState]'s states
 * handling and swipe refresh layout
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RekoScaffold(
    modifier: Modifier = Modifier,
    loadState: LoadState<*>? = null,
    isSwipeRefreshEnabled: Boolean = false,
    onSwipeRefresh: () -> Unit = {},
    onRetryClick: () -> Unit = {},
    topAppBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) = Scaffold(
    modifier = modifier,
    topBar = topAppBar,
    floatingActionButton = floatingActionButton
) {
    val isLoading = loadState?.isLoading == true
    val hasError = loadState?.error != null
    val hasNoInternetError = loadState?.error is ResponseError.NoInternetError
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = loadState?.isSwipeRefresh == true
    )

    SwipeRefresh(
        modifier = Modifier.fillMaxSize(),
        state = swipeRefreshState,
        onRefresh = { onSwipeRefresh() },
        swipeEnabled = isSwipeRefreshEnabled
    ) {
        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
            hasNoInternetError -> {
                ErrorWithIcon(
                    icon = Icons.Filled.WifiOff,
                    text = stringResource(id = R.string.scaffold_no_internet_error_text),
                    onRetryClick = onRetryClick
                )
            }
            hasError -> {
                ErrorWithIcon(
                    icon = Icons.Filled.Warning,
                    text = stringResource(id = R.string.scaffold_error_text),
                    onRetryClick = onRetryClick
                )
            }
            else -> content.invoke()
        }
    }
}

/**
 * Error view with [icon] and [text]
 */
@Composable
fun ErrorWithIcon(
    icon: ImageVector,
    text: String,
    onRetryClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(128.dp),
                imageVector = icon,
                contentDescription = stringResource(id = R.string.content_description_error_icon),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.size(16.dp))

            Text(
                text = text,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.size(16.dp))

            FilledTonalButton(onClick = { onRetryClick() }) {
                Text(text = stringResource(id = R.string.scaffold_retry_text))
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun ErrorPreview() {
    RekoTheme {
        RekoScaffold(loadState = LoadState.error<Unit>(Throwable())) {}
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun NoInternetPreview() {
    RekoTheme {
        RekoScaffold(loadState = LoadState.error<Unit>(ResponseError.NoInternetError())) {}
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun LoadingPreview() {
    RekoTheme {
        RekoScaffold(loadState = LoadState.loading<Unit>()) {}
    }
}