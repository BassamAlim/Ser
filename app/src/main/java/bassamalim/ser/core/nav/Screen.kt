package bassamalim.ser.core.nav

sealed class Screen(val route: String) {

    object About: Screen("about")

    object Main: Screen("main")

    object Settings: Screen("settings")

    object Welcome: Screen("welcome")

    /*data class ###(
    val ##: String, val ##: String="-1"
    ): Screen("###/$##/$##")*/

}