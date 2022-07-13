package dev.weazyexe.reko.ui.screen.auth

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.weazyexe.core.utils.EMPTY_STRING
import dev.weazyexe.reko.R
import dev.weazyexe.reko.ui.common.components.snackbar.ErrorSnackbar
import dev.weazyexe.reko.ui.common.components.textfield.RekoTextField
import dev.weazyexe.reko.ui.theme.AppTypography
import dev.weazyexe.reko.ui.theme.RekoTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * [AuthScreen]'s screen body
 */
@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun AuthBody(
    email: String = EMPTY_STRING,
    password: String = EMPTY_STRING,
    emailError: String = EMPTY_STRING,
    passwordError: String = EMPTY_STRING,
    isLoading: Boolean = false,
    snackbarHostState: SnackbarHostState? = null,
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onSignInClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {}
) {
    val config = LocalConfiguration.current
    val density = LocalDensity.current

    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val (passwordFocusRequester) = FocusRequester.createRefs()
    val screenHeightPx = with(density) { config.screenHeightDp.dp.roundToPx() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
            .verticalScroll(scrollState)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, bottom = 16.dp),
            text = stringResource(id = R.string.auth_app_name_title),
            style = AppTypography.displaySmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(),
            text = stringResource(id = R.string.auth_app_description_text),
            style = AppTypography.bodyLarge
        )

        RekoTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, top = 32.dp)
                .onFocusEvent {
                    scope.launch {
                        delay(250)
                        scrollState.animateScrollTo(screenHeightPx)
                    }
                },
            value = email,
            hint = stringResource(id = R.string.auth_email_text),
            onValueChange = { onEmailChange(it) },
            errorMessage = emailError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { passwordFocusRequester.requestFocus() }
            )
        )
        RekoTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(passwordFocusRequester)
                .onFocusEvent {
                    scope.launch {
                        delay(250)
                        scrollState.animateScrollTo(screenHeightPx)
                    }
                },
            value = password,
            hint = stringResource(id = R.string.auth_password_text),
            onValueChange = { onPasswordChange(it) },
            errorMessage = passwordError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onSignInClick() }
            ),
            hasTogglePasswordButton = true
        )

        Spacer(modifier = Modifier.weight(1f))

        ExtendedFloatingActionButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            onClick = {
                if (!isLoading) {
                    onSignInClick()
                }
            },
            content = {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                } else {
                    Text(text = stringResource(id = R.string.auth_sign_in_text))
                }
            }
        )

        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(bottom = 8.dp),
            onClick = {
                if (!isLoading) {
                    onSignUpClick()
                }
            },
        ) {
            Text(text = stringResource(id = R.string.auth_sign_up_text))
        }
    }

    snackbarHostState?.also { hostState ->
        ErrorSnackbar(
            modifier = Modifier.statusBarsPadding(),
            snackbarHostState = hostState
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_3A
)
@Composable
fun AuthBodyPreview() {
    RekoTheme {
        AuthBody()
    }
}