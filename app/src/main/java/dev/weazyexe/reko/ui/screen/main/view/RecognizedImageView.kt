package dev.weazyexe.reko.ui.screen.main.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import dev.weazyexe.reko.R
import dev.weazyexe.reko.domain.Emotion
import dev.weazyexe.reko.domain.RecognizedImage
import dev.weazyexe.reko.domain.RecognizerType
import dev.weazyexe.reko.ui.theme.RekoTheme
import org.ocpsoft.prettytime.PrettyTime

/**
 * Recognized image view
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecognizedImageView(
    image: RecognizedImage,
    modifier: Modifier = Modifier
) {
    val prettyTime = remember { PrettyTime() }

    val topEmotionDescription = stringResource(id = image.mostPossibleEmotion.asStringResource())
    val recognizerDescription = stringResource(id = image.recognizerType.asStringResource())

    Card(modifier = modifier.fillMaxWidth()) {
        Column {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = rememberImagePainter(image.imageUrl),
                contentScale = ContentScale.Crop,
                contentDescription = stringResource(
                    id = R.string.content_description_recognized_image,
                    topEmotionDescription
                )
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Spacer(modifier = Modifier.size(8.dp))

                Text(text = topEmotionDescription)

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = stringResource(
                        id = R.string.main_recognizer_with_time_text,
                        recognizerDescription,
                        prettyTime.format(image.date)
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    RekoTheme {
        RecognizedImageView(
            RecognizedImage(
                id = "ID",
                imageUrl = "https://bit.ly/3rlanB8",
                recognizerType = RecognizerType.LOCAL,
                emotions = mapOf(Emotion.SURPRISE to 92)
            )
        )
    }
}