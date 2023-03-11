@file:OptIn(ExperimentalAnimationApi::class)

package bassamalim.ser.core.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import bassamalim.ser.core.ui.components.BottomNavItem

val inFromBottom = { _: AnimatedContentScope<NavBackStackEntry> ->
    slideInVertically(
        initialOffsetY = { 500 },
        animationSpec = tween(300)
    ) + fadeIn(animationSpec = tween(200))
}

val outToBottom = { _: AnimatedContentScope<NavBackStackEntry> ->
    slideOutVertically(
        targetOffsetY = { -500 },
        animationSpec = tween(300)
    ) + fadeOut(animationSpec = tween(200))
}

val inFromTop = { _: AnimatedContentScope<NavBackStackEntry> ->
    slideInVertically(
        initialOffsetY = { -500 },
        animationSpec = tween(300)
    ) + fadeIn(animationSpec = tween(200))
}

val outToTop = { _: AnimatedContentScope<NavBackStackEntry> ->
    slideOutVertically(
        targetOffsetY = { 500 },
        animationSpec = tween(300)
    ) + fadeOut(animationSpec = tween(200))
}


val bottomNavBarWeightMap = hashMapOf(
    BottomNavItem.Home.route to 1,
    BottomNavItem.More.route to 5
)

val TabEnter = { an: AnimatedContentScope<NavBackStackEntry> ->
    val from = an.initialState.destination.route
    val to = an.targetState.destination.route
    val fromWeight = bassamalim.ser.core.ui.bottomNavBarWeightMap[from] ?: 0
    val toWeight = bassamalim.ser.core.ui.bottomNavBarWeightMap[to] ?: 0

    if (fromWeight < toWeight) bassamalim.ser.core.ui.inFromLeftTransition
    else bassamalim.ser.core.ui.inFromRightTransition
}

val TabExit = { an: AnimatedContentScope<NavBackStackEntry> ->
    val from = an.initialState.destination.route
    val to = an.targetState.destination.route
    val fromWeight = bassamalim.ser.core.ui.bottomNavBarWeightMap[from] ?: 0
    val toWeight = bassamalim.ser.core.ui.bottomNavBarWeightMap[to] ?: 0

    if (fromWeight < toWeight) bassamalim.ser.core.ui.outToRightTransition
    else bassamalim.ser.core.ui.outToLeftTransition
}

val TabPopEnter = { an: AnimatedContentScope<NavBackStackEntry> ->
    val from = an.initialState.destination.route
    val to = an.targetState.destination.route
    val fromWeight = bassamalim.ser.core.ui.bottomNavBarWeightMap[from] ?: 0
    val toWeight = bassamalim.ser.core.ui.bottomNavBarWeightMap[to] ?: 0

    if (fromWeight < toWeight) bassamalim.ser.core.ui.inFromLeftTransition
    else bassamalim.ser.core.ui.inFromRightTransition
}

val TabPopExit = { an: AnimatedContentScope<NavBackStackEntry> ->
    val from = an.initialState.destination.route
    val to = an.targetState.destination.route
    val fromWeight = bassamalim.ser.core.ui.bottomNavBarWeightMap[from] ?: 0
    val toWeight = bassamalim.ser.core.ui.bottomNavBarWeightMap[to] ?: 0

    if (fromWeight < toWeight) bassamalim.ser.core.ui.outToRightTransition
    else bassamalim.ser.core.ui.outToLeftTransition
}

val inFromRight = { _: AnimatedContentScope<NavBackStackEntry> ->
    bassamalim.ser.core.ui.inFromRightTransition
}

val inFromLeft = { _: AnimatedContentScope<NavBackStackEntry> ->
    bassamalim.ser.core.ui.inFromLeftTransition
}

val outToRight = { _: AnimatedContentScope<NavBackStackEntry> ->
    bassamalim.ser.core.ui.outToRightTransition
}

val outToLeft = { _: AnimatedContentScope<NavBackStackEntry> ->
    bassamalim.ser.core.ui.outToLeftTransition
}

val inFromRightTransition = slideInHorizontally(
    initialOffsetX = { 1000 },
    animationSpec = tween(300)
) + fadeIn(animationSpec = tween(300))

val outToLeftTransition = slideOutHorizontally(
    targetOffsetX = { -500 },
    animationSpec = tween(300)
) + fadeOut(animationSpec = tween(300))

val inFromLeftTransition = slideInHorizontally(
    initialOffsetX = { -1000 },
    animationSpec = tween(300)
) + fadeIn(animationSpec = tween(100))

val outToRightTransition = slideOutHorizontally(
    targetOffsetX = { 500 },
    animationSpec = tween(300)
) + fadeOut(animationSpec = tween(300))