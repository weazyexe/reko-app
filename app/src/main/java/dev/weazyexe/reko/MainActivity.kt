package dev.weazyexe.reko

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.weazyexe.reko.screen.AuthScreen
import dev.weazyexe.reko.screen.MainScreen
import dev.weazyexe.reko.ui.theme.RekoTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RekoTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Root()
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