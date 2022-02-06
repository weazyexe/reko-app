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
import dev.weazyexe.core.ui.Route
import dev.weazyexe.core.utils.extensions.makeEdgeToEdge
import dev.weazyexe.reko.ui.screen.auth.AuthRoute
import dev.weazyexe.reko.ui.screen.auth.AuthScreen
import dev.weazyexe.reko.ui.screen.main.MainRoute
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

    val navigateTo = fun(route: Route) {
        navController.navigate(route.path)
    }

    val back = fun() {
        navController.popBackStack()
    }

    val authRoute = AuthRoute()
    NavHost(navController = navController, startDestination = authRoute.path) {
        composable(authRoute.path) { AuthScreen(navigateTo) }
        composable(MainRoute().path) { MainScreen(navigateTo, back) }
    }
}