package bassamalim.ser.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bassamalim.ser.R
import bassamalim.ser.core.ui.theme.AppTheme

@Composable
fun SearchComponent(
    value: String,
    modifier: Modifier = Modifier,
    hint: String = stringResource(R.string.search),
    onSubmit: () -> Unit = {},
    onValueChange: (String) -> Unit = {}
) {
    val lineColor = AppTheme.colors.weakText
    val layoutDirection = LocalLayoutDirection.current

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .padding(horizontal = 6.dp)
            .drawWithContent {
                drawContent()
                clipRect {
                    val strokeWidth = Stroke.DefaultMiter
                    val y = size.height
                    if (layoutDirection == LayoutDirection.Rtl) {
                        drawLine(
                            brush = SolidColor(lineColor),
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Square,
                            start = Offset.Zero.copy(x = 20F, y = y - 15),
                            end = Offset(x = size.width - 120F, y = y - 15)
                        )
                    }
                    else {
                        drawLine(
                            brush = SolidColor(lineColor),
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Square,
                            start = Offset.Zero.copy(x = 140F, y = y - 15),
                            end = Offset(x = size.width - 20F, y = y - 15)
                        )
                    }
                }
            },
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                tint = AppTheme.colors.weakText,
                contentDescription = "",
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (value != "") {
                IconButton(
                    onClick = { onValueChange("") }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        placeholder = {
            MyText(
                text = hint,
                fontSize = 18.sp,
                textColor = AppTheme.colors.weakText
            )
        },
        singleLine = true,
        shape = RectangleShape, // The TextFiled has rounded corners top left and right by default
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = { onSubmit() }
        ),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            cursorColor = Color.White,
            leadingIconColor = Color.White,
            trailingIconColor = Color.White,
            backgroundColor = AppTheme.colors.background,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}