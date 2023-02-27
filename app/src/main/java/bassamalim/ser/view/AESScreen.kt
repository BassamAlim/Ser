package bassamalim.ser.view

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import bassamalim.ser.R
import bassamalim.ser.enums.Operation
import bassamalim.ser.ui.components.*
import bassamalim.ser.ui.theme.AppTheme
import bassamalim.ser.viewmodel.AESVM
import kotlinx.coroutines.launch

@Composable
fun AESUI(
    vm: AESVM = hiltViewModel()
) {
    val st by vm.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var scrolled = false

    MyParentColumn(
        scroll = false,
        modifier = Modifier
            .padding(top = 6.dp)
            .verticalScroll(scrollState)
    ) {
        AESKeyCard(
            keyAvailable = st.keyAvailable,
            keySaved = st.keySaved,
            key = st.key,
            onCopyKey = vm::onCopyKey,
            onSaveKey = vm::onSaveKey,
            onSelectKey = vm::onSelectKey,
            onGenerateKey = vm::onGenerateKey,
            onImportKey = vm::onImportKey,
        )

        TwoWaySwitch(
            isRight = st.operation == Operation.DECRYPT,
            leftText = stringResource(R.string.encryption),
            rightText = stringResource(R.string.decryption),
            onSwitch = { vm.onOpSwitch(it) }
        )

        MyOutlinedTextField(
            hint = stringResource(
                if (st.operation == Operation.ENCRYPT) R.string.plaintext
                else R.string.ciphertext
            ),
            onValueChange = { vm.onTextChange(it) },
            modifier = Modifier
                .width(350.dp)
                .height(200.dp)
        )

        PrimaryPillBtn(
            text =
                if (st.operation == Operation.ENCRYPT) stringResource(R.string.encrypt)
                else stringResource(R.string.decrypt),
            onClick = vm::onExecute
        )

        if (st.result.isNotEmpty()) {
            MyText(
                text = stringResource(
                    if (st.operation == Operation.ENCRYPT) R.string.ciphertext
                    else R.string.plaintext
                ),
                fontSize = 22.sp,
                textAlign = TextAlign.Start,
                textColor = AppTheme.colors.strongText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 5.dp, start = 26.dp)
            )

            MyRow {
                SelectionContainer {
                    FramedText(
                        text = st.result,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(bottom = 20.dp, start = 5.dp, end = 5.dp)
                            .onGloballyPositioned { layoutCoordinates ->
                                if (!scrolled) {
                                    coroutineScope.launch {
                                        scrollState.animateScrollTo(
                                            layoutCoordinates.positionInWindow().y.toInt()
                                        )
                                        scrolled = true
                                    }
                                }
                            }
                    )
                }

                CopyBtn(onClick = vm::onCopyResult)
            }
        }

        KeyPickerDialog(
            st.keyPickerShown,
            vm.keyNames,
            onCancel = vm::onKeyPickerCancel,
            onKeySelected = { vm.onKeySelected(it) },
        )

        SaveKeyDialog(
            st.saveDialogShown,
            st.nameAlreadyExists,
            onTextChange = { vm.onSaveDialogNameChange(it) },
            onSubmit = { vm.onSaveDialogSubmit(it) },
            onCancel = vm::onSaveDialogCancel
        )
    }
}

@Composable
fun AESKeyCard(
    keyAvailable: Boolean,
    keySaved: Boolean,
    key: String,
    modifier: Modifier = Modifier,
    onCopyKey: () -> Unit,
    onSaveKey: () -> Unit,
    onSelectKey: () -> Unit,
    onGenerateKey: () -> Unit,
    onImportKey: () -> Unit
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(if (expandedState) 180f else 0f)

    MyClickableSurface(
        modifier = modifier
            .animateContentSize(
                animationSpec = TweenSpec(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        color = AppTheme.colors.primary,
        onClick = { expandedState = !expandedState }
    ) {
        MyFatColumn {
            MyRow(
                arrangement = Arrangement.SpaceBetween,
                padding = PaddingValues(start = 6.dp, end = 12.dp)
            ) {
                MyText(
                    text = stringResource(
                        if (keyAvailable) R.string.key
                        else R.string.no_key_available
                    ),
                    fontSize = 22.sp,
                    textAlign = TextAlign.Start,
                    textColor = AppTheme.colors.strongText,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 10.dp, bottom = 10.dp, start = 16.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (keyAvailable) CopyBtn(onClick = onCopyKey)

                    if (!keySaved) SaveBtn(onClick = onSaveKey)

                    Box(
                        modifier = Modifier.padding(start = 20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = stringResource(R.string.select),
                            tint = AppTheme.colors.text,
                            modifier = Modifier
                                .size(40.dp)
                                .rotate(rotationState)
                        )
                    }
                }
            }

            if (expandedState) {
                if (keyAvailable) {
                    SelectionContainer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 10.dp)
                    ) {
                        MyText(
                            key,
                            fontSize = 20.sp
                        )
                    }
                }

                SecondaryPillBtn(
                    text = stringResource(R.string.select_key),
                    textColor =
                    if (keyAvailable) AppTheme.colors.text
                    else AppTheme.colors.accent,
                    onClick = onSelectKey
                )

                SecondaryPillBtn(
                    text = stringResource(
                        if (keyAvailable) R.string.generate_new_key
                        else R.string.generate_key
                    ),
                    textColor =
                    if (keyAvailable) AppTheme.colors.text
                    else AppTheme.colors.accent,
                    onClick = onGenerateKey
                )

                SecondaryPillBtn(
                    text = stringResource(R.string.import_key),
                    textColor =
                    if (keyAvailable) AppTheme.colors.text
                    else AppTheme.colors.accent,
                    modifier = Modifier.padding(bottom = 10.dp),
                    onClick = onImportKey
                )
            }
        }
    }
}