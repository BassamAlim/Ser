package bassamalim.ser.features.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import bassamalim.ser.R
import bassamalim.ser.core.ui.*
import bassamalim.ser.core.ui.components.*
import bassamalim.ser.core.ui.theme.AppTheme
import bassamalim.ser.features.aes.AESUI
import bassamalim.ser.features.home.HomeUI
import bassamalim.ser.features.keys.KeysUI
import bassamalim.ser.features.more.MoreUI
import bassamalim.ser.features.rsa.RSAUI
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainUI(
    nc: NavHostController = rememberAnimatedNavController(),
    vm: MainVM = hiltViewModel()
) {
    val st by vm.uiState.collectAsState()
    val bottomNc = rememberAnimatedNavController()

    MyScaffold(
        title = stringResource(R.string.app_name),
        topBar = { TopBar() },
        bottomBar = { MyBottomNavigation(bottomNc) }
    ) {
        NavigationGraph(nc, bottomNc, it)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    bottomNavController: NavHostController,
    padding: PaddingValues
) {
    AnimatedNavHost(
        bottomNavController,
        startDestination = BottomNavItem.Home.route,
        modifier = Modifier.padding(padding)
    ) {
        composable(
            route = BottomNavItem.AES.route,
            enterTransition = TabEnter,
            exitTransition = TabExit,
            popEnterTransition = TabPopEnter,
            popExitTransition = TabPopExit
        ) {
            AESUI(
                vm = hiltViewModel()
            )
        }

        composable(
            route = BottomNavItem.Home.route,
            enterTransition = TabEnter,
            exitTransition = TabExit,
            popEnterTransition = TabPopEnter,
            popExitTransition = TabPopExit
        ) {
            HomeUI()
        }

        composable(
            route = BottomNavItem.Keys.route,
            enterTransition = TabEnter,
            exitTransition = TabExit,
            popEnterTransition = TabPopEnter,
            popExitTransition = TabPopExit
        ) {
            KeysUI(
                nc = navController,
                vm = hiltViewModel()
            )
        }

        composable(
            route = BottomNavItem.More.route,
            enterTransition = TabEnter,
            exitTransition = TabExit,
            popEnterTransition = TabPopEnter,
            popExitTransition = TabPopExit
        ) {
            MoreUI(
                nc = navController,
                vm = hiltViewModel()
            )
        }

        composable(
            route = BottomNavItem.RSA.route,
            enterTransition = TabEnter,
            exitTransition = TabExit,
            popEnterTransition = TabPopEnter,
            popExitTransition = TabPopExit
        ) {
            RSAUI(
                vm = hiltViewModel()
            )
        }
    }
}

@Composable
fun TopBar() {
    TopAppBar(
        backgroundColor = AppTheme.colors.primary,
        elevation = 8.dp
    ) {
        Box(
            Modifier.fillMaxSize()
        ) {
            MyText(
                stringResource(R.string.app_name),
                textColor = AppTheme.colors.onPrimary,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 14.dp)
            )

            Icon(
                painter = painterResource(R.drawable.foreground_icon),
                contentDescription = "Logo",
                tint = AppTheme.colors.accent,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(14.dp)
            )
        }
    }
}