package dev.weazyexe.reko.ui.common.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsPadding
import dev.weazyexe.reko.ui.theme.RekoTheme

/**
 * Base layout for bottom sheets
 */
@Composable
fun BaseBottomSheet(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .navigationBarsPadding()
    ) {
        Spacer(modifier = Modifier.size(8.dp))

        ShapeLayout(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .alpha(0.5f),
            shape = RoundedCornerShape(4.dp),
            width = 64.dp,
            height = 4.dp,
            color = MaterialTheme.colorScheme.surfaceVariant
        )

        Spacer(modifier = Modifier.size(8.dp))

        content(this)
    }
}

@Composable
private fun ShapeLayout(
    shape: Shape,
    width: Dp,
    height: Dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(width, height)
            .clip(shape)
            .background(color)
    )
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun Preview() {
    RekoTheme {
        BaseBottomSheet {
            TextButton(onClick = {}) {
                Text(text = "Some bottom sheet content")
            }
        }
    }
}