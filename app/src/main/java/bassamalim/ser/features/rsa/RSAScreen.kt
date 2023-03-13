package bassamalim.ser.features.rsa

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import bassamalim.ser.R
import bassamalim.ser.core.enums.Algorithm
import bassamalim.ser.core.enums.Operation
import bassamalim.ser.core.models.RSAKeyPair
import bassamalim.ser.core.ui.components.*
import bassamalim.ser.core.ui.theme.AppTheme
import bassamalim.ser.features.keyPicker.KeyPickerDlg
import bassamalim.ser.features.rsaKeyGen.RSAKeyGenDlg
import kotlinx.coroutines.launch

@Composable
fun RSAUI(
    vm: RSAVM = hiltViewModel()
) {
    val st by vm.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var scrolled = false

    DisposableEffect(key1 = vm) {
        vm.onStart()
        onDispose {}
    }

    MyParentColumn(
        scroll = false,
        modifier = Modifier
            .padding(top = 6.dp)
            .verticalScroll(scrollState)
    ) {
        RSAKeyCard(
            st = st,
            onCopyPublicKey = vm::onCopyPublicKey,
            onCopyPrivateKey = vm::onCopyPrivateKey,
            onSelectKey = vm::onSelectKey,
            onNewKey = vm::onNewKey
        )

        TwoWaySwitch(
            isRight = st.operation == Operation.DECRYPT,
            leftText = stringResource(R.string.encryption),
            rightText = stringResource(R.string.decryption),
            onSwitch = { vm.onOpSwitch(it) }
        )

        MyOutlinedTextField(
            label = stringResource(
                if (st.operation == Operation.ENCRYPT) R.string.plaintext
                else R.string.ciphertext
            ),
            onValueChange = { vm.onTextChange(it) },
            modifier = Modifier
                .width(350.dp)
                .height(200.dp)
        )

        PrimaryPillBtn(
            text = stringResource(
                if (st.operation == Operation.ENCRYPT) R.string.encrypt
                else R.string.decrypt
            ),
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

        KeyPickerDlg(
            shown = st.keyPickerShown,
            algorithm = Algorithm.RSA,
            onCancel = vm::onKeyPickerCancel,
            mainOnKeySelected = { vm.onKeySelected(it as RSAKeyPair) },
        )

        RSAKeyGenDlg(
            shown = st.newKeyDialogShown,
            onCancel = vm::onNewKeyDlgCancel,
            onSubmit = { vm.onNewKeyDlgSubmit(it) }
        )
    }
}

@Composable
fun RSAKeyCard(
    st: RSAState,
    onCopyPublicKey: () -> Unit,
    onCopyPrivateKey: () -> Unit,
    onSelectKey: () -> Unit,
    onNewKey: () -> Unit
) {
    ExpandableCard(
        title = "${stringResource(R.string.key_pair)}: ${st.keyName}",
        expandedContent = {
            KeySpace(
                titleResId = R.string.public_key,
                keyValue = st.publicKey,
                onCopy = onCopyPublicKey
            )

            KeySpace(
                titleResId = R.string.private_key,
                keyValue = st.privateKey,
                onCopy = onCopyPrivateKey
            )

            MyRow {
                SecondaryPillBtn(
                    text = stringResource(R.string.new_keypair),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 5.dp),
                    onClick = onNewKey
                )

                SecondaryPillBtn(
                    text = stringResource(R.string.select_keypair),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 5.dp),
                    onClick = onSelectKey
                )
            }
        }
    )
}