package bassamalim.ser.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import bassamalim.ser.R

val tajwal = FontFamily(
    Font(R.font.tajawal_regular, weight = FontWeight.Normal),
    Font(R.font.tajawal_extra_bold, weight = FontWeight.ExtraBold),
    Font(R.font.tajawal_bold, weight = FontWeight.Bold),
    Font(R.font.tajawal_medium, weight = FontWeight.Medium),
    Font(R.font.tajawal_light, weight = FontWeight.Light),
    Font(R.font.tajawal_extra_light, weight = FontWeight.ExtraLight),
    Font(R.font.tajawal_black, weight = FontWeight.Black),
)

data class AppTypography(
    val h1: TextStyle = TextStyle(
        fontFamily = tajwal,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    val subtitle: TextStyle = TextStyle(
        fontFamily = tajwal,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    val body: TextStyle = TextStyle(
        fontFamily = tajwal,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    val button: TextStyle = TextStyle(
        fontFamily = tajwal,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    val caption: TextStyle = TextStyle(
        fontFamily = tajwal,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
)

internal val LocalTypography = staticCompositionLocalOf { AppTypography() }

val Int.nsp
    @Composable
    get() = (this / LocalDensity.current.fontScale * 1.15).sp
