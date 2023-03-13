package bassamalim.ser.core.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bassamalim.ser.R
import bassamalim.ser.core.ui.theme.AppTheme

@Composable
fun MySurface(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(vertical = 6.dp, horizontal = 8.dp),
    cornerRadius: Dp = 10.dp,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(padding),
        shape = RoundedCornerShape(cornerRadius),
        color = AppTheme.colors.surface,
        elevation = 8.dp
    ) {
        content()
    }
}

@Composable
fun MyClickableSurface(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(vertical = 3.dp, horizontal = 8.dp),
    cornerRadius: Dp = 10.dp,
    elevation: Dp = 6.dp,
    color: Color = AppTheme.colors.surface,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(padding),
        shape = RoundedCornerShape(cornerRadius),
        color = color,
        elevation = elevation,
    ) {
        Box(
            Modifier.clickable { onClick() }
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyBtnSurface(
    text: String,
    modifier: Modifier = Modifier,
    innerVPadding: Dp = 10.dp,
    fontSize: TextUnit = 20.sp,
    iconBtn: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 8.dp),
        shape = RoundedCornerShape(10.dp),
        color = AppTheme.colors.surface,
        elevation = 6.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = innerVPadding, horizontal = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MyText(
                text = text,
                fontSize = fontSize,
                modifier = Modifier
                    .weight(1F)
                    .padding(10.dp)
            )

            iconBtn()
        }
    }
}