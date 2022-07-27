package dev.weazyexe.reko.ui.common.components.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
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
import dev.weazyexe.reko.utils.noRippleClickable

/**
 * Base scaffold for app. It includes all the [LoadState]'s states
 * handling and swipe refresh layout
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun RekoScaffold(
    modifier: Modifier = Modifier,
    loadState: LoadState<*>? = null,
    isSwipeRefreshEnabled: Boolean = false,
    onSwipeRefresh: () -> Unit = {},
    onRetryClick: () -> Unit = {},
    bottomSheetState: ModalBottomSheetState? = null,
    bottomSheetContent: @Composable ColumnScope.() -> Unit = {},
    topAppBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    if (bottomSheetState != null) {
        ModalBottomSheetLayout(
            sheetContent = bottomSheetContent,
            sheetState = bottomSheetState,
            sheetShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            sheetBackgroundColor = MaterialTheme.colorScheme.surface,
            sheetContentColor = MaterialTheme.colorScheme.onSurface
        ) {
            RekoScaffoldBody(
                modifier = modifier,
                topAppBar = topAppBar,
                floatingActionButton = floatingActionButton,
                onRetryClick = onRetryClick,
                onSwipeRefresh = onSwipeRefresh,
                isSwipeRefreshEnabled = isSwipeRefreshEnabled,
                loadState = loadState,
                content = content
            )
        }
    } else {
        RekoScaffoldBody(
            modifier = modifier,
            topAppBar = topAppBar,
            floatingActionButton = floatingActionButton,
            onRetryClick = onRetryClick,
            onSwipeRefresh = onSwipeRefresh,
            isSwipeRefreshEnabled = isSwipeRefreshEnabled,
            loadState = loadState,
            content = content
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RekoScaffoldBody(
    modifier: Modifier,
    topAppBar: @Composable () -> Unit,
    floatingActionButton: @Composable () -> Unit,
    onSwipeRefresh: () -> Unit,
    isSwipeRefreshEnabled: Boolean,
    loadState: LoadState<*>?,
    onRetryClick: () -> Unit,
    content: @Composable () -> Unit
) {
    @Composable
    fun withScaffold(content: @Composable () -> Unit) {
        Scaffold(
            modifier = modifier,
            topBar = topAppBar,
            floatingActionButton = floatingActionButton
        ) {
            val swipeRefreshState = rememberSwipeRefreshState(
                isRefreshing = loadState?.isSwipeRefresh == true
            )

            SwipeRefresh(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding()),
                state = swipeRefreshState,
                onRefresh = { onSwipeRefresh() },
                swipeEnabled = isSwipeRefreshEnabled,
                content = content
            )
        }
    }

    val isLoading = loadState?.isLoading == true
    val isTransparentLoading = loadState?.isTransparent == true
    val isSwipeRefreshLoading = loadState?.isSwipeRefresh == true
    val hasError = loadState?.error != null
    val hasNoInternetError = loadState?.error is ResponseError.NoInternetError

    when {
        isTransparentLoading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                withScaffold {
                    content.invoke()
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
                        .noRippleClickable()
                )

                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        isSwipeRefreshLoading -> withScaffold { content.invoke() }
        isLoading -> withScaffold {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        hasNoInternetError -> withScaffold {
            ErrorWithIcon(
                icon = Icons.Filled.WifiOff,
                text = stringResource(id = R.string.scaffold_no_internet_error_text),
                onRetryClick = onRetryClick
            )
        }
        hasError -> withScaffold {
            ErrorWithIcon(
                icon = Icons.Filled.Warning,
                text = stringResource(id = R.string.scaffold_error_text),
                onRetryClick = onRetryClick
            )
        }
        else -> withScaffold { content.invoke() }
    }
}

/**
 * Error view with [icon] and [text]
 */
@Composable
private fun ErrorWithIcon(
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

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun ErrorPreview() {
    RekoTheme {
        RekoScaffold(loadState = LoadState.error<Unit>(Throwable())) {}
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun NoInternetPreview() {
    RekoTheme {
        RekoScaffold(
            loadState = LoadState.error<Unit>(ResponseError.NoInternetError()),
            content = {}
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun LoadingPreview() {
    RekoTheme {
        RekoScaffold(
            loadState = LoadState.loading<Unit>(),
            content = {}
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun TransparentLoadingPreview() {
    RekoTheme {
        RekoScaffold(
            loadState = LoadState.loading<Unit>(isTransparent = true),
            content = {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { }) {
                        Text(text = "Button1")
                    }
                    Button(onClick = { }) {
                        Text(text = "Button2")
                    }
                }
            }
        )
    }
}