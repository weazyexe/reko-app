package dev.weazyexe.reko

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import dev.weazyexe.core.ui.Route
import dev.weazyexe.core.utils.extensions.makeEdgeToEdge
import dev.weazyexe.reko.ui.screen.auth.AuthRoute
import dev.weazyexe.reko.ui.screen.auth.AuthScreen
import dev.weazyexe.reko.ui.screen.main.MainRoute
import dev.weazyexe.reko.ui.screen.main.MainScreen
import dev.weazyexe.reko.ui.theme.RekoTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val appViewModel by viewModels<AppViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeEdgeToEdge()
        installSplashScreen()
        val hasUser = appViewModel.checkUser()

        setContent {
            RekoTheme {
                ProvideWindowInsets {
                    Surface {
                        Root(
                            initialRoute = if (hasUser) {
                                MainRoute()
                            } else {
                                AuthRoute()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Root(initialRoute: Route) {
    val navController = rememberNavController()

    val navigateTo = fun(route: Route) {
        navController.navigate(route.path)
    }

    val back = fun() {
        navController.popBackStack()
    }

    NavHost(navController = navController, startDestination = initialRoute.path) {
        composable(AuthRoute().path) { AuthScreen(navigateTo) }
        composable(MainRoute().path) { MainScreen(navigateTo) }
    }
}