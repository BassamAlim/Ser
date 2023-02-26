package bassamalim.ser.state

data class AboutState(
    val isDevModeOn: Boolean = false,
    val isDatabaseRebuilt: Boolean = false,
    val shouldShowRebuilt: Int = 0
)