package dev.weazyexe.reko.ui.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import dev.weazyexe.core.ui.LoadState
import dev.weazyexe.reko.R
import dev.weazyexe.reko.domain.Emotion
import dev.weazyexe.reko.domain.RecognizedImage
import dev.weazyexe.reko.domain.Recognizer
import dev.weazyexe.reko.ui.common.components.scaffold.RekoScaffold
import dev.weazyexe.reko.ui.screen.main.view.RecognizedImageView
import dev.weazyexe.reko.ui.theme.RekoTheme

/**
 * [MainScreen]'s screen body
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainBody(
    imagesLoadState: LoadState<List<RecognizedImage>> = LoadState(data = emptyList()),
    onRecognizeClick: () -> Unit = {}
) {
    RekoScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        loadState = imagesLoadState,
        isSwipeRefreshEnabled = true,
        onSwipeRefresh = {},
        onRetryClick = {},
        topAppBar = {
            MediumTopAppBar(
                title = { Text(stringResource(id = R.string.main_recognized_images_text)) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.navigationBarsPadding(),
                onClick = { onRecognizeClick() }
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
                        .padding(16.dp)
                        .navigationBarsPadding()
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
                        recognizer = Recognizer.LOCAL,
                        emotions = mapOf(Emotion.SURPRISE to 92)
                    ),
                    RecognizedImage(
                        id = "ID",
                        imageUrl = "https://bit.ly/3rlanB8",
                        recognizer = Recognizer.UNKNOWN,
                        emotions = mapOf(Emotion.ANGER to 84)
                    ),
                    RecognizedImage(
                        id = "ID",
                        imageUrl = "https://bit.ly/3rlanB8",
                        recognizer = Recognizer.SKY_BIOMETRY,
                        emotions = mapOf(Emotion.HAPPINESS to 100)
                    )
                )
            )
        )
    }
}