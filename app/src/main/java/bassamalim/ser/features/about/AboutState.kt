package bassamalim.ser.features.about

data class AboutState(
    val isDevModeOn: Boolean = false,
    val isDatabaseRebuilt: Boolean = false,
    val shouldShowRebuilt: Int = 0
)