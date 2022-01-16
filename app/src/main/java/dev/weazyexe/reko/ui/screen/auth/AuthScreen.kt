package dev.weazyexe.reko.ui.screen.auth

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.navigationBarsPadding
import dev.weazyexe.core.utils.ReceiveEffect
import dev.weazyexe.core.utils.extensions.toast
import dev.weazyexe.reko.MAIN_SCREEN
import dev.weazyexe.reko.R
import dev.weazyexe.reko.ui.common.RekoTextField
import dev.weazyexe.reko.ui.screen.auth.AuthAction.OnPasswordChange
import dev.weazyexe.reko.ui.screen.auth.AuthAction.OnUsernameChange
import dev.weazyexe.reko.ui.screen.auth.AuthEffect.Initial
import dev.weazyexe.reko.ui.screen.auth.AuthEffect.ShowMessage
import dev.weazyexe.reko.ui.theme.RekoTheme

@Composable
fun AuthScreen(
    navigateTo: (String) -> Unit = {},
    context: Context = LocalContext.current
) {
    val viewModel = viewModel(modelClass = AuthViewModel::class.java)
    val state by viewModel.uiState.collectAsState()

    val effectState = viewModel.effects.collectAsState(Initial)
    ReceiveEffect(effectState) {
        when (this) {
            is ShowMessage -> {
                context.toast(message)
            }
            else -> {
                // Do nothing
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 72.dp)
        ) {
            RekoTextField(
                modifier = Modifier.padding(bottom = 8.dp),
                value = state.username,
                hint = stringResource(id = R.string.auth_email_text),
                onValueChange = {
                    viewModel.emit(OnUsernameChange(it))
                }
            )
            RekoTextField(
                value = state.password,
                hint = stringResource(id = R.string.auth_password_text),
                onValueChange = {
                    viewModel.emit(OnPasswordChange(it))
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        }

        ExtendedFloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
                .navigationBarsPadding(),
            text = { Text(text = stringResource(id = R.string.auth_sign_in_text)) },
            onClick = { navigateTo(MAIN_SCREEN) },
            icon = { Icon(imageVector = Icons.Filled.Done, contentDescription = null) }
        )
    }
}

@Preview(
    uiMode = UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_3A
)
@Composable
fun AuthPreview() {
    RekoTheme {
        AuthScreen()
    }
}