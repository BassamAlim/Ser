@file:OptIn(ExperimentalAnimationApi::class)

package bassamalim.ser.core.nav

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import bassamalim.ser.core.ui.*
import bassamalim.ser.view.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@Composable
fun Navigator(thenTo: String? = null, shouldWelcome: Boolean = false) {
    val navController = rememberAnimatedNavController()

    val startDest =
        if (shouldWelcome) Screen.Welcome.route
        else Screen.Main.route

    NavGraph(navController, startDest)

    if (thenTo != null) navController.navigate(thenTo)
}

@Composable
fun NavGraph(navController: NavHostController, startDest: String) {
	AnimatedNavHost(
        navController = navController,
        startDestination = startDest
    ) {
        composable(
            route = Screen.About.route,
            enterTransition = bassamalim.ser.core.ui.inFromBottom,
            exitTransition = bassamalim.ser.core.ui.outToBottom,
            popEnterTransition = bassamalim.ser.core.ui.inFromTop,
            popExitTransition = bassamalim.ser.core.ui.outToTop
        ) {
            AboutUI(
                vm = hiltViewModel()
            )
        }

        composable(
            route = Screen.Main.route,
            enterTransition = bassamalim.ser.core.ui.inFromBottom,
            exitTransition = bassamalim.ser.core.ui.outToBottom,
            popEnterTransition = bassamalim.ser.core.ui.inFromTop,
            popExitTransition = bassamalim.ser.core.ui.outToTop
        ) {
            MainUI(
                vm = hiltViewModel(),
                nc = navController
            )
        }

        composable(
            route = Screen.Settings.route,
            enterTransition = bassamalim.ser.core.ui.inFromBottom,
            exitTransition = bassamalim.ser.core.ui.outToBottom,
            popEnterTransition = bassamalim.ser.core.ui.inFromTop,
            popExitTransition = bassamalim.ser.core.ui.outToTop
        ) {
            SettingsUI(
                vm = hiltViewModel()
            )
        }

        composable(
            route = Screen.Welcome.route,
            enterTransition = bassamalim.ser.core.ui.inFromBottom,
            exitTransition = bassamalim.ser.core.ui.outToBottom,
            popEnterTransition = bassamalim.ser.core.ui.inFromTop,
            popExitTransition = bassamalim.ser.core.ui.outToTop
        ) {
            WelcomeUI(
                vm = hiltViewModel(),
                nc = navController
            )
        }

        /*composable(
            route = Screen.###(
                "{##}"
            ).route,
            arguments = listOf(
                navArgument("##") { type = NavType.StringType }
            ),
            enterTransition = inFromBottom,
            exitTransition = outToBottom,
            popEnterTransition = inFromTop,
            popExitTransition = outToTop
        ) {
            ###UI(
                vm = hiltViewModel(),
                nc = navController
            )
        }*/
    }
}


// custom nav type because the default one crashes
/*
val IntArrType: NavType<IntArray> = object : NavType<IntArray>(false) {
    override fun put(bundle: Bundle, key: String, value: IntArray) {
        bundle.putIntArray(key, value)
    }

    override fun get(bundle: Bundle, key: String): IntArray {
        return bundle.getIntArray(key) as IntArray
    }

    override fun parseValue(value: String): IntArray {
        return Gson().fromJson(value, IntArray::class.java)
    }
}*/
