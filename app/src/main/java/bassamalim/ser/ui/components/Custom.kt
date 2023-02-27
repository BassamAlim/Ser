package bassamalim.ser.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import bassamalim.ser.R
import bassamalim.ser.ui.theme.AppTheme

@Composable
fun TwoWaySwitch(
    isRight: Boolean,
    leftText: String,
    rightText: String,
    onSwitch: (Boolean) -> Unit
) {
    var state by remember { mutableStateOf(isRight) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        MyText(leftText)

        Switch(
            checked = state,
            onCheckedChange = {
                state = it
                onSwitch(it)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = AppTheme.colors.accent,
                checkedTrackColor = AppTheme.colors.altAccent,
                uncheckedThumbColor = AppTheme.colors.accent,
                uncheckedTrackColor = AppTheme.colors.altAccent
            )
        )

        MyText(rightText)
    }
}

@Composable
fun CopyBtn(
    modifier: Modifier = Modifier,
    size: Dp = 26.dp,
    tint: Color = AppTheme.colors.strongText,
    onClick: () -> Unit
) {
    MyIconBtn(
        iconId = R.drawable.ic_copy,
        descriptionResId = R.string.copy,
        modifier = modifier,
        size = size,
        tint = tint,
        onClick = onClick
    )
}

@Composable
fun SaveBtn(
    modifier: Modifier = Modifier,
    size: Dp = 30.dp,
    tint: Color = AppTheme.colors.strongText,
    onClick: () -> Unit
) {
    MyIconBtn(
        iconId = R.drawable.ic_save,
        descriptionResId = R.string.save,
        modifier = modifier,
        size = size,
        tint = tint,
        onClick = onClick
    )
}

@Composable
fun SaveKeyDialog(
    shown: Boolean,
    nameExists: Boolean,
    onTextChange: (String) -> Unit,
    onSubmit: (String) -> Unit,
    onCancel: () -> Unit
) {
    var text by remember { mutableStateOf("") }

    MyDialog(
        shown = shown,
        onDismiss = onCancel
    ) {
        MyColumn {
            DialogTitle(R.string.save_key)

            MyOutlinedTextField(
                hint = stringResource(R.string.key_name),
                onValueChange = {
                    text = it
                    onTextChange(it)
                },
                modifier = Modifier.padding(vertical = 10.dp)
            )

            if (nameExists) {
                MyText(
                    text = stringResource(R.string.key_name_exists),
                    textColor = bassamalim.ser.ui.theme.Negative
                )
            }

            MyRow {
                SecondaryPillBtn(
                    text = stringResource(R.string.cancel),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 5.dp),
                    onClick = onCancel
                )

                SecondaryPillBtn(
                    text = stringResource(R.string.save),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 5.dp),
                    onClick = { onSubmit(text) }
                )
            }
        }
    }
}

@Composable
fun KeyPickerDialog(
    shown: Boolean,
    keyNames: List<String>,
    onKeySelected: (Int) -> Unit,
    onCancel: () -> Unit
) {
    MyDialog(
        shown = shown,
        intrinsicHeight = false,  // because of the lazy column
        onDismiss = onCancel
    ) {
        MyColumn(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
        ) {
            DialogTitle(textResId = R.string.pick_key)

            MyLazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 30.dp),
                lazyList = {
                    itemsIndexed(keyNames) { i, keyName ->
                        MyClickableText(
                            text = keyName,
                            onClick = { onKeySelected(i) }
                        )

                        if (i != keyNames.lastIndex) MyHorizontalDivider()
                    }
                }
            )

            SecondaryPillBtn(
                text = stringResource(R.string.cancel),
                onClick = onCancel
            )
        }
    }
}