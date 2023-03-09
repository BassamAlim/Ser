package bassamalim.ser.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import bassamalim.ser.R
import bassamalim.ser.models.AESKey
import bassamalim.ser.models.RSAKeyPair
import bassamalim.ser.ui.components.*
import bassamalim.ser.ui.theme.AppTheme
import bassamalim.ser.viewmodel.KeysVM
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun KeysUI(
    nc: NavController = rememberAnimatedNavController(),
    vm: KeysVM = hiltViewModel()
) {
    val st by vm.uiState.collectAsState()

    DisposableEffect(key1 = vm) {
        vm.onStart()
        onDispose {}
    }

    MyColumn {
        PrimaryPillBtn(text = stringResource(R.string.public_key_repo)) {
            // TODO
        }

        MyHorizontalDivider()

        MyColumn {
            MyRow(
                arrangement = Arrangement.SpaceBetween
            ) {
                CategoryTitle(R.string.aes_keys)

                MyIconBtn(
                    imageVector = Icons.Default.Add,
                    size = 28.dp,
                    tint = AppTheme.colors.text,
                    onClick = { vm.onAddAESKey() }
                )
            }

            MyLazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .background(AppTheme.colors.secondary),
                lazyList = {
                    itemsIndexed(st.aesKeys) { i, key ->
                        AESExpandableItem(
                            vm = vm,
                            key = key,
                            idx = i
                        )
                    }
                }
            )

            MyRow(
                arrangement = Arrangement.SpaceBetween
            ) {
                CategoryTitle(R.string.rsa_keys)

                MyIconBtn(
                    imageVector = Icons.Default.Add,
                    size = 28.dp,
                    tint = AppTheme.colors.text,
                    onClick = { vm.onAddRSAKey() }
                )
            }

            MyLazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 10.dp)
                    .background(AppTheme.colors.secondary),
                lazyList = {
                    itemsIndexed(st.rsaKeys) { i, keyPair ->
                        RSAExpandableItem(
                            vm = vm,
                            idx = i,
                            keyPair = keyPair
                        )
                    }
                }
            )
        }
    }

    AESKeyAddDialog(
        shown = st.aesKeyAddDialogShown,
        nameExists = st.aesNameAlreadyExists,
        valueInvalid = st.invalidAESKey,
        onNameChange = { vm.onAESKeyAddDialogNameChange(it) },
        onCancel = { vm.onAESKeyAddDialogCancel() },
        onSubmit = { name, value ->
            vm.onAESKeyAddDialogSubmit(name, value)
        }
    )

    RSAKeyAddDialog(
        shown = st.rsaKeyAddDialogShown,
        nameExists = st.rsaNameAlreadyExists,
        valueInvalid = st.invalidRSAKey,
        onNameChange = { vm.onRSAKeyAddDialogNameChange(it) },
        onCancel = { vm.onRSAKeyAddDialogCancel() },
        onSubmit = { name, public, private ->
            vm.onRSAKeyAddDialogSubmit(name, public, private)
        }
    )
}

@Composable
fun AESKeyAddDialog(
    shown: Boolean,
    nameExists: Boolean,
    valueInvalid: Boolean,
    onNameChange: (String) -> Unit,
    onCancel: () -> Unit,
    onSubmit: (String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }

    MyDialog(
        shown = shown,
        onDismiss = onCancel
    ) {
        MyColumn(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
        ) {
            DialogTitle(
                textResId = R.string.new_key,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            MyColumn(
                modifier = Modifier
                    .height(260.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                MyOutlinedTextField(
                    hint = stringResource(R.string.key_name),
                    onValueChange = {
                        name = it
                        onNameChange(it)
                    },
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                if (nameExists) {
                    MyText(
                        text = stringResource(R.string.key_name_exists),
                        textColor = bassamalim.ser.ui.theme.Negative
                    )
                }

                MyOutlinedTextField(
                    hint = stringResource(R.string.key_value),
                    modifier = Modifier.padding(vertical = 6.dp),
                    onValueChange = { value = it }
                )

                if (valueInvalid) {
                    MyText(
                        text = stringResource(R.string.key_value_invalid),
                        textColor = bassamalim.ser.ui.theme.Negative
                    )
                }
            }

            MyRow(
                modifier = Modifier.padding(top = 6.dp)
            ) {
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
                    onClick = { onSubmit(name, value) }
                )
            }
        }
    }
}


@Composable
fun RSAKeyAddDialog(
    shown: Boolean,
    nameExists: Boolean,
    valueInvalid: Boolean,
    onNameChange: (String) -> Unit,
    onCancel: () -> Unit,
    onSubmit: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var public by remember { mutableStateOf("") }
    var private by remember { mutableStateOf("") }

    MyDialog(
        shown = shown,
        onDismiss = onCancel
    ) {
        MyColumn(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
        ) {
            DialogTitle(
                textResId = R.string.new_keypair,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            MyColumn(
                modifier = Modifier
                    .height(350.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                MyOutlinedTextField(
                    hint = stringResource(R.string.keypair_name),
                    onValueChange = {
                        name = it
                        onNameChange(it)
                    },
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                if (nameExists) {
                    MyText(
                        text = stringResource(R.string.key_name_exists),
                        textColor = bassamalim.ser.ui.theme.Negative
                    )
                }

                MyOutlinedTextField(
                    hint = stringResource(R.string.public_key),
                    onValueChange = { public = it },
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                MyOutlinedTextField(
                    hint = stringResource(R.string.private_key),
                    onValueChange = { private = it },
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                if (valueInvalid) {
                    MyText(
                        text = stringResource(R.string.key_value_invalid),
                        textColor = bassamalim.ser.ui.theme.Negative
                    )
                }
            }

            MyRow(
                modifier = Modifier.padding(top = 6.dp)
            ) {
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
                    onClick = { onSubmit(name, public, private) }
                )
            }
        }
    }
}

@Composable
fun AESExpandableItem(
    vm: KeysVM,
    key: AESKey,
    idx: Int
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(if (expandedState) 180f else 0f)

    MyColumn(
        modifier = Modifier
            .animateContentSize(
                animationSpec = TweenSpec(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
            .clickable { expandedState = !expandedState }
    ) {
        MyRow(
            Modifier.padding(all = 16.dp)
        ) {
            MyText(
                key.name,
                Modifier.weight(1f),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = stringResource(R.string.select),
                tint = AppTheme.colors.text,
                modifier = Modifier.rotate(rotationState)
            )
        }

        // expanded content
        if (expandedState) {
            MyColumn {
                MyRow {
                    SelectionContainer(
                        modifier = Modifier.weight(1f)
                    ) {
                        MyText(
                            key.asString(),
                            fontSize = 18.sp
                        )
                    }

                    CopyBtn(onClick = { vm.onAESKeyCopy(idx) })
                }

                MyRow {
                    MyClickableText(
                        text = stringResource(R.string.rename),
                        onClick = { vm.onAESKeyRename(idx) }
                    )

                    MyClickableText(
                        text = stringResource(R.string.remove),
                        textColor = bassamalim.ser.ui.theme.Negative,
                        isEnabled = idx != 0,
                        onClick = { vm.onAESKeyRemove(idx) }
                    )

                    MyClickableText(
                        text = stringResource(R.string.set_primary),
                        onClick = { vm.onAESKeySetPrimary(idx) }
                    )
                }
            }
        }
    }

    MyHorizontalDivider()
}

@Composable
fun RSAExpandableItem(
    vm: KeysVM,
    keyPair: RSAKeyPair,
    idx: Int
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(if (expandedState) 180f else 0f)

    MyColumn(
        modifier = Modifier
            .animateContentSize(
                animationSpec = TweenSpec(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
            .clickable { expandedState = !expandedState }
    ) {
        MyRow(
            Modifier.padding(all = 16.dp)
        ) {
            MyText(
                keyPair.name,
                Modifier.weight(1f),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = stringResource(R.string.select),
                tint = AppTheme.colors.text,
                modifier = Modifier.rotate(rotationState)
            )
        }

        // expanded content
        if (expandedState) {
            MyColumn {
                MyRow(
                    padding = PaddingValues(start = 6.dp, end = 12.dp)
                ) {
                    MyText(
                        text = stringResource(R.string.public_key),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Start,
                        textColor = AppTheme.colors.strongText,
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 10.dp, start = 16.dp)
                    )

                    CopyBtn(
                        size = 22.dp,
                        onClick = { vm.onRSAPublicKeyCopy(idx) }
                    )
                }

                SelectionContainer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(all = 10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .verticalScroll(rememberScrollState())
                        .background(AppTheme.colors.surface)
                ) {
                    MyText(
                        keyPair.key.publicAsString(),
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 10.dp)
                    )
                }

                MyRow(
                    padding = PaddingValues(start = 6.dp, end = 12.dp)
                ) {
                    MyText(
                        text = stringResource(R.string.private_key),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Start,
                        textColor = AppTheme.colors.strongText,
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 10.dp, start = 16.dp)
                    )

                    CopyBtn(
                        size = 22.dp,
                        onClick = { vm.onRSAPrivateKeyCopy(idx) }
                    )
                }

                SelectionContainer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(all = 10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .verticalScroll(rememberScrollState())
                        .background(AppTheme.colors.surface)
                ) {
                    MyText(
                        keyPair.key.privateAsString(),
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 10.dp)
                    )
                }

                MyRow {
                    MyClickableText(
                        text = stringResource(R.string.rename),
                        onClick = { vm.onRSAKeyRename(idx) }
                    )

                    MyClickableText(
                        text = stringResource(R.string.remove),
                        textColor = bassamalim.ser.ui.theme.Negative,
                        isEnabled = idx != 0,
                        onClick = { vm.onRSAKeyRemove(idx) }
                    )

                    MyClickableText(
                        text = stringResource(R.string.set_primary),
                        onClick = { vm.onRSAKeySetPrimary(idx) }
                    )
                }
            }
        }
    }

    MyHorizontalDivider()
}