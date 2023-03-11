package bassamalim.ser.core.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Grey = Color(0xFF484849)
val Positive = Color(0xFF1BA739)
val Negative = Color(0xFFD13434)

val BG = Color(0xFF111317)
val Primary = Color(0xFF161A1E)
val OnPrimary = Color(0xFFBCC2C7)
val Secondary = Color(0xFF212934)
val OnSecondary = Color(0xFFBCBEC0)
val Surface = Color(0xFF212728)
val OnSurface = Color(0xFF959A9E)
val Accent = Color(0xFFDE6006)
val AltAccent = Color(0x3CBB5207)  // 3C = 60% opacity
val Text = Color(0xFFBCC2C7)
val StrongText = Color(0xFFFCFBF8)
val WeakText = Color(0xFF6E6E6E)

class AppColors(
    background: Color = BG,
    primary: Color = Primary,
    onPrimary: Color = OnPrimary,
    secondary: Color = Secondary,
    onSecondary: Color = OnSecondary,
    surface: Color = Surface,
    onSurface: Color = OnSurface,
    accent: Color = Accent,
    altAccent: Color = AltAccent,
    text: Color = Text,
    strongText: Color = StrongText,
    weakText: Color = WeakText
) {
    var background by mutableStateOf(background)
        private set
    var primary by mutableStateOf(primary)
        private set
    var onPrimary by mutableStateOf(onPrimary)
        private set
    var secondary by mutableStateOf(secondary)
        private set
    var onSecondary by mutableStateOf(onSecondary)
        private set
    var surface by mutableStateOf(surface)
        private set
    var onSurface by mutableStateOf(onSurface)
        private set
    var accent by mutableStateOf(accent)
        private set
    var altAccent by mutableStateOf(altAccent)
        private set
    var text by mutableStateOf(text)
        private set
    var strongText by mutableStateOf(strongText)
        private set
    var weakText by mutableStateOf(weakText)
        private set
}

val LocalColors = staticCompositionLocalOf { AppColors() }