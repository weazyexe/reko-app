package dev.weazyexe.reko

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import dev.weazyexe.core.utils.extensions.makeEdgeToEdge
import dev.weazyexe.reko.ui.screen.auth.AuthScreen
import dev.weazyexe.reko.ui.screen.main.MainScreen
import dev.weazyexe.reko.ui.theme.RekoTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeEdgeToEdge()

        setContent {
            RekoTheme {
                ProvideWindowInsets {
                    Surface {
                        Root()
                    }
                }
            }
        }
    }
}

@Composable
fun Root() {
    val navController = rememberNavController()

    val navigateTo = fun(destination: String) {
        navController.navigate(destination)
    }

    val back = fun() {
        navController.popBackStack()
    }

    NavHost(navController = navController, startDestination = AUTH_SCREEN) {
        composable(AUTH_SCREEN) { AuthScreen(navigateTo) }
        composable(MAIN_SCREEN) { MainScreen(navigateTo, back) }
    }
}