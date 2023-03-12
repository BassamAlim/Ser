package bassamalim.ser.core.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bassamalim.ser.core.ui.theme.AppTheme
import bassamalim.ser.core.ui.theme.tajwal

@Composable
fun MyText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 20.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Center,
    textColor: Color = AppTheme.colors.text,
    fontFamily: FontFamily = tajwal,
    softWrap: Boolean = true
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = fontSize,
        style = TextStyle(
            fontFamily = fontFamily,
            color = textColor,
            fontWeight = fontWeight,
            textAlign = textAlign,
            lineHeight = fontSize * 1.4
        ),
        softWrap = softWrap
    )
}

@Composable
fun MyText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 20.sp,
    textColor: Color = AppTheme.colors.text,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Center,
    fontFamily: FontFamily = tajwal
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = fontSize,
        style = TextStyle(
            fontFamily = fontFamily,
            color = textColor,
            fontWeight = fontWeight,
            textAlign = textAlign,
            lineHeight = fontSize * 1.4,
        )
    )
}

@Composable
fun MyClickableText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 20.sp,
    textColor: Color = AppTheme.colors.strongText,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Center,
    fontFamily: FontFamily = tajwal,
    isEnabled: Boolean = true,
    innerPadding: PaddingValues = PaddingValues(vertical = 6.dp, horizontal = 15.dp),
    onClick: () -> Unit
) {
    Box(
        modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { if (isEnabled) onClick() }
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(innerPadding),
            fontSize = fontSize,
            style = TextStyle(
                fontFamily = fontFamily,
                color = if (isEnabled) textColor else bassamalim.ser.core.ui.theme.Grey,
                fontWeight = fontWeight,
                textAlign = textAlign,
                lineHeight = fontSize * 1.4
            )
        )
    }
}

@Composable
fun FramedText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 20.sp,
    textColor: Color = AppTheme.colors.text,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Center,
    fontFamily: FontFamily = tajwal,
    softWrap: Boolean = true
) {
    Text(
        text = text,
        fontSize = fontSize,
        style = TextStyle(
            fontFamily = fontFamily,
            color = textColor,
            fontWeight = fontWeight,
            textAlign = textAlign,
            lineHeight = fontSize * 1.4
        ),
        softWrap = softWrap,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, AppTheme.colors.strongText, RoundedCornerShape(10.dp))
            .padding(10.dp)
    )
}

@Composable
fun RowScope.CategoryTitle(
    textResId: Int,
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(0.dp),
    fontSize: TextUnit = 18.sp,
    textColor: Color = AppTheme.colors.strongText
) {
    MyText(
        text = stringResource(textResId),
        modifier = modifier
            .weight(1f)
            .padding(top = 10.dp, bottom = 10.dp, start = 12.dp)
            .padding(padding),
        fontSize = fontSize,
        textAlign = TextAlign.Start,
        textColor = textColor
    )
}

@Composable
fun DialogTitle(
    textResId: Int,
    modifier: Modifier = Modifier
) {
    MyText(
        text = stringResource(textResId),
        modifier = modifier.fillMaxWidth(),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )
}