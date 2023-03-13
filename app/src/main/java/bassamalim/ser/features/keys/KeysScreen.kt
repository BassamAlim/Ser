package bassamalim.ser.features.keys

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import bassamalim.ser.R
import bassamalim.ser.core.models.AESKey
import bassamalim.ser.core.models.RSAKeyPair
import bassamalim.ser.core.ui.components.*
import bassamalim.ser.core.ui.theme.AppTheme
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
            vm.onPublicKeyStore(nc)
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
                            st = st,
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
                        textColor = bassamalim.ser.core.ui.theme.Negative
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
                        textColor = bassamalim.ser.core.ui.theme.Negative
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
                        textColor = bassamalim.ser.core.ui.theme.Negative
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
                        textColor = bassamalim.ser.core.ui.theme.Negative
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
    ExpandableItem(
        title = key.name,
        expandedContent = {
            MyColumn {
                KeySpace(
                    titleResId = R.string.key,
                    keyValue = key.asString(),
                    onCopy = { vm.onAESKeyCopy(idx) }
                )

                MyRow {
                    MyClickableText(
                        text = stringResource(R.string.rename),
                        isEnabled = idx != 0,
                        onClick = { vm.onAESKeyRename(idx) }
                    )

                    MyClickableText(
                        text = stringResource(R.string.remove),
                        textColor = bassamalim.ser.core.ui.theme.Negative,
                        isEnabled = idx != 0,
                        onClick = { vm.onAESKeyRemove(idx) }
                    )
                }
            }
        }
    )
}

@Composable
fun RSAExpandableItem(
    vm: KeysVM,
    st: KeysState,
    keyPair: RSAKeyPair,
    idx: Int
) {
    ExpandableItem(
        title = keyPair.name,
        extraVisible = {
            if (keyPair.key.publicAsString() == st.publishedKeyValue) {
                Box(
                    modifier = Modifier.padding(end = 14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(bassamalim.ser.core.ui.theme.Positive)
                    )
                }
            }
        },
        expandedContent = {
            MyColumn {
                KeySpace(
                    titleResId = R.string.public_key,
                    keyValue = keyPair.key.publicAsString(),
                    onCopy = { vm.onRSAPublicKeyCopy(idx) }
                )

                KeySpace(
                    titleResId = R.string.private_key,
                    keyValue = keyPair.key.privateAsString(),
                    onCopy = { vm.onRSAPrivateKeyCopy(idx) }
                )

                MyRow {
                    MyClickableText(
                        text = stringResource(R.string.rename),
                        isEnabled = idx != 0,
                        onClick = { vm.onRSAKeyRename(idx) }
                    )

                    MyClickableText(
                        text = stringResource(R.string.remove),
                        textColor = bassamalim.ser.core.ui.theme.Negative,
                        isEnabled = idx != 0 &&
                                keyPair.key.publicAsString() != st.publishedKeyValue,
                        onClick = { vm.onRSAKeyRemove(idx) }
                    )
                }
            }
        }
    )
}