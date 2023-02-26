package bassamalim.ser.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bassamalim.ser.ui.theme.AppTheme

@Composable
fun MyTopBar(
    title: String = "",
    backgroundColor: Color = AppTheme.colors.primary,
    contentColor: Color = AppTheme.colors.onPrimary,
    onBack: (() -> Unit)? = null
) {
    TopAppBar(
        backgroundColor = backgroundColor,
        elevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box {
            MyBackBtn(onBack)

            Row(
                Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProvideTextStyle(value = MaterialTheme.typography.h6) {
                    CompositionLocalProvider(
                        LocalContentAlpha provides ContentAlpha.high,
                    ) {
                        MyText(
                            text = title,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            textColor = contentColor
                        )
                    }
                }
            }
        }
    }
}