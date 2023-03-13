package bassamalim.ser.features.keys

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
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
import bassamalim.ser.features.aesKeyGen.AESKeyGenDlg
import bassamalim.ser.features.keyRename.KeyRenameDlg
import bassamalim.ser.features.rsaKeyGen.RSAKeyGenDlg
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
        PrimaryPillBtn(text = stringResource(R.string.public_key_store)) {
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
                        AESItem(
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
                        RSAItem(
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

    AESKeyGenDlg(
        shown = st.aesKeyAddDialogShown,
        onCancel = vm::onAESKeyAddDialogCancel,
        mainOnSubmit = { vm.onAESKeyAddDialogSubmit() }
    )

    RSAKeyGenDlg(
        shown = st.rsaKeyAddDialogShown,
        onCancel = vm::onRSAKeyAddDialogCancel,
        onSubmit = { vm.onRSAKeyAddDialogSubmit() }
    )

    KeyRenameDlg(
        shown = st.keyRenameDialogShown,
        algorithm = st.keyRenameDialogAlgorithm,
        oldName = st.keyRenameDialogOldName,
        onCancel = vm::onKeyRenameDialogCancel,
        mainOnSubmit = { vm.onKeyRenameDialogSubmit(it) }
    )
}

@Composable
fun AESItem(
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
fun RSAItem(
    vm: KeysVM,
    st: KeysState,
    keyPair: RSAKeyPair,
    idx: Int
) {
    ExpandableItem(
        title = keyPair.name,
        extraVisible = {
            if (keyPair.name == st.publishedKeyName) {
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
                                keyPair.name != st.publishedKeyName,
                        onClick = { vm.onRSAKeyRemove(idx) }
                    )
                }
            }
        }
    )
}