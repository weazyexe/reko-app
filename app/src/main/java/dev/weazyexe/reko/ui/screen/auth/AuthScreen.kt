package dev.weazyexe.reko.ui.screen.auth

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import dev.weazyexe.core.ui.Route
import dev.weazyexe.core.utils.ReceiveEffect
import dev.weazyexe.reko.ui.screen.auth.AuthAction.OnEmailChange
import dev.weazyexe.reko.ui.screen.auth.AuthAction.OnPasswordChange
import dev.weazyexe.reko.ui.screen.main.MainRoute
import dev.weazyexe.reko.ui.theme.RekoTheme

@Composable
fun AuthScreen(
    navigateTo: (Route) -> Unit = {},
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val state by authViewModel.uiState.collectAsState()
    val effectState = authViewModel.effects.collectAsState(null)

    ReceiveEffect(effectState) {
        when (this) {
            is AuthEffect.GoToMainScreen -> navigateTo(MainRoute())
            else -> {
                // Do nothing
            }
        }
    }

    AuthBody(
        email = state.email,
        password = state.password,
        isLoading = state.signInLoadState.isLoading,
        emailError = state.emailError.orEmpty(),
        passwordError = state.passwordError.orEmpty(),
        onEmailChange = { authViewModel.emit(OnEmailChange(it)) },
        onPasswordChange = { authViewModel.emit(OnPasswordChange(it)) },
        onSignInClick = { authViewModel.emit(AuthAction.OnSignInClick) },
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