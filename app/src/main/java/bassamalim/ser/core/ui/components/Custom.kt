package bassamalim.ser.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bassamalim.ser.R
import bassamalim.ser.core.ui.theme.AppTheme

@Composable
fun TwoWaySwitch(
    isRight: Boolean,
    leftText: String,
    rightText: String,
    modifier: Modifier = Modifier,
    onSwitch: (Boolean) -> Unit
) {
    var state by remember { mutableStateOf(isRight) }

    Row(
        modifier = modifier.fillMaxWidth(),
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
                label = stringResource(R.string.key_name),
                onValueChange = {
                    text = it
                    onTextChange(it)
                },
                modifier = Modifier.padding(vertical = 10.dp)
            )

            if (nameExists) {
                MyText(
                    text = stringResource(R.string.key_name_exists),
                    textColor = bassamalim.ser.core.ui.theme.Negative
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
fun KeySpace(
    titleResId: Int,
    keyValue: String,
    onCopy: () -> Unit
) {
    MyRow(
        padding = PaddingValues(start = 6.dp, end = 12.dp)
    ) {
        MyText(
            text = stringResource(titleResId),
            textAlign = TextAlign.Start,
            modifier = Modifier
                .weight(1f)
                .padding(top = 10.dp, start = 16.dp)
        )

        CopyBtn(onClick = onCopy)
    }

    SelectionContainer(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(20.dp, 100.dp)
            .padding(all = 10.dp)
            .clip(RoundedCornerShape(10.dp))
            .verticalScroll(rememberScrollState())
            .background(AppTheme.colors.surface)
    ) {
        MyText(
            keyValue,
            fontSize = 20.sp,
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 10.dp)
        )
    }
}