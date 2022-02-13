package dev.weazyexe.reko.ui.screen.auth

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import dev.weazyexe.core.ui.Route
import dev.weazyexe.core.utils.ReceiveEffect
import dev.weazyexe.reko.ui.screen.auth.AuthAction.*
import dev.weazyexe.reko.ui.screen.main.MainRoute
import dev.weazyexe.reko.ui.theme.RekoTheme
import kotlinx.coroutines.launch

/**
 * Authorization screen
 */
@Composable
fun AuthScreen(
    navigateTo: (Route) -> Unit = {},
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val state by authViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    ReceiveEffect(authViewModel.effects) {
        when (this) {
            is AuthEffect.GoToMainScreen -> navigateTo(MainRoute())
            is AuthEffect.ShowMessage -> scope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    AuthBody(
        email = state.email,
        password = state.password,
        isLoading = state.signInLoadState.isLoading,
        emailError = state.emailError.orEmpty(),
        passwordError = state.passwordError.orEmpty(),
        snackbarHostState = snackbarHostState,
        onEmailChange = { authViewModel.emit(OnEmailChange(it)) },
        onPasswordChange = { authViewModel.emit(OnPasswordChange(it)) },
        onSignInClick = { authViewModel.emit(OnSignInClick) },
        onSignUpClick = { }
    )
}

@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_3A
)
@Composable
fun AuthScreenPreview() {
    RekoTheme {
        AuthBody()
    }
}