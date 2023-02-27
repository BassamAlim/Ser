package bassamalim.ser.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import bassamalim.ser.R
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
                        MyRow(
                            arrangement = Arrangement.SpaceBetween
                        ) {
                            MyClickableText(text = key.name) {
                                vm.onAESKeyClk(i)
                            }

                            CopyBtn(
                                tint = AppTheme.colors.text,
                                onClick = { vm.onAESKeyCopy(i) }
                            )
                        }

                        MyHorizontalDivider()
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
                    itemsIndexed(st.rsaKeys) { i, key ->
                        MyRow(
                            arrangement = Arrangement.SpaceBetween
                        ) {
                            MyClickableText(text = key.name) {
                                vm.onRSAKeyClk(i)
                            }
                        }

                        MyHorizontalDivider()
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
            DialogTitle(textResId = R.string.new_key)

            MyOutlinedTextField(
                hint = stringResource(R.string.key_name),
                onValueChange = {
                    name = it
                    onNameChange(it)
                },
                modifier = Modifier.padding(vertical = 10.dp)
            )

            if (nameExists) {
                MyText(
                    text = stringResource(R.string.key_name_exists),
                    textColor = bassamalim.ser.ui.theme.Negative
                )
            }

            MyOutlinedTextField(
                hint = stringResource(R.string.key_value),
                modifier = Modifier.padding(vertical = 10.dp),
                onValueChange = { value = it }
            )

            if (valueInvalid) {
                MyText(
                    text = stringResource(R.string.key_value_invalid),
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
            DialogTitle(textResId = R.string.new_keypair)

            MyOutlinedTextField(
                hint = stringResource(R.string.keypair_name),
                onValueChange = {
                    name = it
                    onNameChange(it)
                },
                modifier = Modifier.padding(vertical = 10.dp)
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
                modifier = Modifier.padding(vertical = 10.dp)
            )

            MyOutlinedTextField(
                hint = stringResource(R.string.private_key),
                onValueChange = { private = it },
                modifier = Modifier.padding(vertical = 10.dp)
            )

            if (valueInvalid) {
                MyText(
                    text = stringResource(R.string.key_value_invalid),
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
                    onClick = { onSubmit(name, public, private) }
                )
            }
        }
    }
}
