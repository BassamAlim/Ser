package bassamalim.ser.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties
import bassamalim.ser.core.ui.theme.AppTheme

@Composable
fun MyFormField(
    value: String,
    title: String,
    modifier: Modifier = Modifier,
    fillFraction: Float = 0.85f,
    isEnabled: Boolean = true,
    onClick: () -> Unit = {},
    onValueChange: (String) -> Unit = {}
) {
    TextField(
        value = value,
        modifier = modifier
            .fillMaxWidth(fillFraction)
            .height(85.dp)
            .padding(vertical = 10.dp)
            .shadow(4.dp, RoundedCornerShape(10.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.textFieldColors(
            textColor = AppTheme.colors.strongText,
            backgroundColor = AppTheme.colors.secondary,
            cursorColor = AppTheme.colors.text,
            disabledLabelColor = AppTheme.colors.secondary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        onValueChange = onValueChange,
        label = {
            MyText(
                text = title,
                modifier = Modifier.padding(bottom = 4.dp),
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
                textColor = AppTheme.colors.text
            )
        },
        textStyle = TextStyle(
            fontSize = 17.sp,
            color = AppTheme.colors.strongText
        ),
        singleLine = true,
        enabled = isEnabled,
        trailingIcon = {
            if (value.isNotEmpty() && isEnabled) {
                IconButton(
                    onClick = { onValueChange("") }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Clear",
                        tint = AppTheme.colors.text
                    )
                }
            }
        }
    )
}

@Composable
fun MySuggestiveFormField(
    textState: String,
    title: String,
    suggestions: List<String>,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    fillFraction: Float = 0.85f,
    onSuggestionChosen: (String) -> Unit = {},
    onValueChange: (String) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val focusManager = LocalFocusManager.current

    Column {
        MyFormField(
            value = textState,
            title = title,
            modifier = modifier
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                },
            isEnabled = isEnabled,
            fillFraction = fillFraction
        ) {
            onValueChange(it)

            expanded = true
        }

        if (suggestions.isNotEmpty()) {
            MaterialTheme(
                shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(8.dp)),
                colors = MaterialTheme.colors.copy(surface = AppTheme.colors.surface)
            ) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                        .heightIn(0.dp, 150.dp),
                    properties = PopupProperties(focusable = false)
                ) {
                    suggestions.forEach { text ->
                        DropdownMenuItem(
                            onClick = {
                                onSuggestionChosen(text)
                                expanded = false
                                focusManager.clearFocus()
                            }
                        ) {
                            MyText(
                                text,
                                fontSize = 16.sp,
                                textColor = AppTheme.colors.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyOutlinedTextField(
    hint: String,
    modifier: Modifier = Modifier,
    fontSize : TextUnit = 18.sp,
    isEnabled: Boolean = true,
    onValueChange: (String) -> Unit = {}
) {
    var textState by remember { mutableStateOf("") }

    OutlinedTextField(
        value = textState,
        modifier = modifier.padding(vertical = 10.dp),
        textStyle = TextStyle(
            fontSize = fontSize
        ),
        onValueChange = {
            textState = it
            onValueChange(it)
        },
        label = {
            MyText(text = hint)
        },
        shape = RoundedCornerShape(15.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = AppTheme.colors.strongText,
            unfocusedBorderColor = AppTheme.colors.text,
            textColor = AppTheme.colors.strongText,
            disabledLabelColor = AppTheme.colors.weakText,
            cursorColor = AppTheme.colors.accent
        ),
        trailingIcon = {
            if (textState.isNotEmpty() && isEnabled) {
                IconButton(
                    onClick = {
                        textState = ""
                        onValueChange("")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Clear",
                        tint = AppTheme.colors.text
                    )
                }
            }
        }
    )
}