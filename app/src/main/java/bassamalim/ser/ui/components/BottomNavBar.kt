package bassamalim.ser.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import bassamalim.ser.R
import bassamalim.ser.ui.theme.AppTheme
import bassamalim.ser.ui.theme.nsp

sealed class BottomNavItem(val route: String, val titleResId: Int, val iconResId: Int) {
    object Home: BottomNavItem("home", R.string.title_home, R.drawable.ic_home)
    object AES: BottomNavItem("aes", R.string.title_aes, R.drawable.ic_aes)
    object RSA: BottomNavItem("rsa", R.string.title_rsa, R.drawable.ic_rsa)
    object Keys: BottomNavItem("keys", R.string.keys, R.drawable.ic_key)
    object More: BottomNavItem("more", R.string.title_more, R.drawable.ic_more)
}

@Composable
fun MyBottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.AES,
        BottomNavItem.RSA,
        BottomNavItem.Keys,
        BottomNavItem.More
    )

    BottomNavigation(
        backgroundColor = AppTheme.colors.primary,
        elevation = 12.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            BottomNavigationItem(
                label = {
                    MyText(
                        stringResource(item.titleResId),
                        fontSize = 9.nsp,
                        textColor = AppTheme.colors.accent,
                        softWrap = false
                    )
                },
                alwaysShowLabel = false,
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconResId),
                        contentDescription = stringResource(item.titleResId),
                        modifier = Modifier.size(24.dp)
                    )
                },
                selectedContentColor = AppTheme.colors.accent,
                unselectedContentColor = AppTheme.colors.onPrimary,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}