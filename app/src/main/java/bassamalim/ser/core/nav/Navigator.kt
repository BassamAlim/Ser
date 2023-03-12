@file:OptIn(ExperimentalAnimationApi::class)

package bassamalim.ser.core.nav

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import bassamalim.ser.core.ui.*
import bassamalim.ser.features.keyStore.KeyStoreUI
import bassamalim.ser.features.main.MainUI
import bassamalim.ser.view.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@Composable
fun Navigator(thenTo: String? = null) {
    val navController = rememberAnimatedNavController()

    NavGraph(navController)

    if (thenTo != null) navController.navigate(thenTo)
}

@Composable
fun NavGraph(navController: NavHostController) {
	AnimatedNavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(
            route = Screen.About.route,
            enterTransition = inFromBottom,
            exitTransition = outToBottom,
            popEnterTransition = inFromTop,
            popExitTransition = outToTop
        ) {
            AboutUI()
        }

        composable(
            route = Screen.KeyStore.route,
            enterTransition = inFromBottom,
            exitTransition = outToBottom,
            popEnterTransition = inFromTop,
            popExitTransition = outToTop
        ) {
            KeyStoreUI()
        }

        composable(
            route = Screen.Main.route,
            enterTransition = inFromBottom,
            exitTransition = outToBottom,
            popEnterTransition = inFromTop,
            popExitTransition = outToTop
        ) {
            MainUI(
                nc = navController
            )
        }
    }
}