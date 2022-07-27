package dev.weazyexe.reko.ui.screen.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.weazyexe.core.ui.LoadState
import dev.weazyexe.reko.R
import dev.weazyexe.reko.domain.Emotion
import dev.weazyexe.reko.domain.RecognizedImage
import dev.weazyexe.reko.domain.RecognizerType
import dev.weazyexe.reko.ui.common.components.scaffold.RekoScaffold
import dev.weazyexe.reko.ui.common.components.snackbar.DefaultSnackbar
import dev.weazyexe.reko.ui.common.components.snackbar.ErrorSnackbar
import dev.weazyexe.reko.ui.screen.main.bottomsheet.PhotoPickerBottomSheet
import dev.weazyexe.reko.ui.screen.main.view.RecognizedImageView
import dev.weazyexe.reko.ui.theme.RekoTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * [MainScreen]'s screen body
 */
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
fun MainBody(
    imagesLoadState: LoadState<List<RecognizedImage>> = LoadState(data = emptyList()),
    errorSnackbarHostState: SnackbarHostState? = null,
    messageSnackbarHostState: SnackbarHostState? = null,
    scope: CoroutineScope? = null,
    onCameraClick: () -> Unit = {},
    onGalleryClick: () -> Unit = {},
    onRetryClick: () -> Unit = {},
    onSwipeRefresh: () -> Unit = {}
) {
    val bottomSheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )

    RekoScaffold(
        modifier = Modifier.fillMaxSize(),
        loadState = imagesLoadState,
        isSwipeRefreshEnabled = true,
        onSwipeRefresh = onSwipeRefresh,
        onRetryClick = onRetryClick,
        bottomSheetState = bottomSheetState,
        bottomSheetContent = {
            PhotoPickerBottomSheet(
                onCameraClick = {
                    scope?.launch {
                        bottomSheetState.hide()
                        onCameraClick()
                    }
                },
                onGalleryClick = {
                    scope?.launch {
                        bottomSheetState.hide()
                        onGalleryClick()
                    }
                }
            )
        },
        topAppBar = {
            // FIXME in the future use contentPadding instead when it releases
            MediumTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = { Text(stringResource(id = R.string.main_recognized_images_text)) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.navigationBarsPadding(),
                onClick = {
                    scope?.launch { bottomSheetState.show() }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.content_description_recognize_emotions_button)
                )
            }
        }
    ) {
        val images = imagesLoadState.data
        when {
            images != null && images.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        text = stringResource(id = R.string.main_no_images_text),
                        textAlign = TextAlign.Center
                    )
                }
            }
            images != null -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = WindowInsets.navigationBars.asPaddingValues()
                ) {
                    items(images.size) { index ->
                        RecognizedImageView(
                            modifier = Modifier.padding(bottom = 16.dp),
                            image = images[index]
                        )
                    }
                }
            }
        }
    }

    errorSnackbarHostState?.also { hostState ->
        ErrorSnackbar(
            modifier = Modifier.statusBarsPadding(),
            snackbarHostState = hostState
        )
    }

    messageSnackbarHostState?.also { hostState ->
        DefaultSnackbar(
            modifier = Modifier.statusBarsPadding(),
            snackbarHostState = hostState
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun EmptyPreview() {
    RekoTheme {
        MainBody()
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun ListPreview() {
    RekoTheme {
        MainBody(
            imagesLoadState = LoadState.data(
                listOf(
                    RecognizedImage(
                        id = "ID",
                        imageUrl = "https://bit.ly/3rlanB8",
                        recognizerType = RecognizerType.LOCAL,
                        emotions = mapOf(Emotion.SURPRISE to 92)
                    ),
                    RecognizedImage(
                        id = "ID",
                        imageUrl = "https://bit.ly/3rlanB8",
                        recognizerType = RecognizerType.UNKNOWN,
                        emotions = mapOf(Emotion.ANGER to 84)
                    ),
                    RecognizedImage(
                        id = "ID",
                        imageUrl = "https://bit.ly/3rlanB8",
                        recognizerType = RecognizerType.SKY_BIOMETRY,
                        emotions = mapOf(Emotion.HAPPINESS to 100)
                    )
                )
            )
        )
    }
}