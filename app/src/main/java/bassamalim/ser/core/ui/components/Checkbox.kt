package bassamalim.ser.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import bassamalim.ser.core.ui.theme.AppTheme

@Composable
fun MyCheckbox(
    text: String,
    textColor: Color = AppTheme.colors.text,
    initialState: Boolean = false,
    arrangement: Arrangement.Horizontal = Arrangement.Start,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    var state by remember { mutableStateOf(initialState) }

    MyRow(
        arrangement = arrangement
    ) {
        Checkbox(
            checked = state,
            onCheckedChange = {
                state = it
                onCheckedChange(it)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = AppTheme.colors.accent,
                uncheckedColor = AppTheme.colors.text
            )
        )

        MyText(
            text,
            textColor = textColor
        )
    }
}