package dev.weazyexe.reko.ui.screen.main.bottomsheet

import androidx.compose.foundation.clickable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import dev.weazyexe.reko.R
import dev.weazyexe.reko.ui.common.components.bottomsheet.BaseBottomSheet
import dev.weazyexe.reko.ui.theme.RekoTheme

/**
 * Photo picker bottom sheet
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PhotoPickerBottomSheet(
    onCameraClick: () -> Unit = {},
    onGalleryClick: () -> Unit = {}
) {
    BaseBottomSheet {
        ListItem(
            modifier = Modifier.clickable { onCameraClick() },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Camera,
                    contentDescription = stringResource(id = R.string.content_description_camera_icon)
                )
            },
            text = {
                Text(text = stringResource(id = R.string.main_open_camera_text))
            }
        )

        ListItem(
            modifier = Modifier.clickable { onGalleryClick() },
            icon = {
                Icon(
                    imageVector = Icons.Filled.PhotoAlbum,
                    contentDescription = stringResource(id = R.string.content_description_gallery_icon)
                )
            },
            text = {
                Text(text = stringResource(id = R.string.main_open_gallery_text))
            }
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun Preview() {
    RekoTheme {
        PhotoPickerBottomSheet()
    }
}