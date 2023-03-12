package bassamalim.ser.core.nav

sealed class Screen(val route: String) {
    object About: Screen("about")
    object KeyStore: Screen("keystore")
    object Main: Screen("main")
}