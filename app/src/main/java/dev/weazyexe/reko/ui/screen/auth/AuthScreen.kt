package dev.weazyexe.reko.ui.screen.auth

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
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
import dev.weazyexe.reko.ui.theme.Typography

@Composable
fun AuthScreen(
    navigateTo: (String) -> Unit = {},
    context: Context = LocalContext.current,
    viewModel: AuthViewModel = viewModel()
) {
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
            .fillMaxSize()
            .navigationBarsWithImePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .statusBarsPadding()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp, bottom = 16.dp),
                text = stringResource(id = R.string.auth_app_name_title),
                style = Typography.displaySmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
                text = stringResource(id = R.string.auth_app_description_text),
                style = Typography.bodyLarge
            )

            RekoTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, top = 32.dp),
                value = state.username,
                hint = stringResource(id = R.string.auth_email_text),
                onValueChange = {
                    viewModel.emit(OnUsernameChange(it))
                }
            )
            RekoTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.password,
                hint = stringResource(id = R.string.auth_password_text),
                onValueChange = {
                    viewModel.emit(OnPasswordChange(it))
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                text = { Text(text = stringResource(id = R.string.auth_sign_in_text)) },
                onClick = { navigateTo(MAIN_SCREEN) }
            )

            TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 8.dp),
                onClick = { /* TODO */ }
            ) {
                Text(text = stringResource(id = R.string.auth_sign_up_text))
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_3A
)
@Composable
fun AuthPreview() {
    RekoTheme {
        AuthScreen(viewModel = AuthViewModel())
    }
}