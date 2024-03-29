package bassamalim.ser.features.rsa

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import bassamalim.ser.R
import bassamalim.ser.core.enums.Algorithm
import bassamalim.ser.core.enums.Operation
import bassamalim.ser.core.ui.components.*
import bassamalim.ser.core.ui.theme.AppTheme
import bassamalim.ser.features.keyPicker.KeyPickerDlg
import bassamalim.ser.features.rsaKeyGen.RSAKeyGenDlg

@Composable
fun RSAUI(
    vm: RSAVM = hiltViewModel()
) {
    val st by vm.uiState.collectAsState()
    val ctx = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> vm.onFileImportResult(uri) }
    )

    DisposableEffect(key1 = vm) {
        vm.onStart(coroutineScope, scrollState)
        onDispose {}
    }

    MyParentColumn(
        scroll = false,
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        RSAKeyCard(
            st = st,
            onCopyPublicKey = vm::onCopyPublicKey,
            onCopyPrivateKey = vm::onCopyPrivateKey,
            onNewKey = vm::onNewKey,
            onSelectKey = vm::onSelectKey
        )

        TwoWaySwitch(
            isRight = st.operation == Operation.DECRYPT,
            leftText = stringResource(R.string.encryption),
            rightText = stringResource(R.string.decryption),
            modifier = Modifier.padding(bottom = 30.dp),
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
                .height(180.dp)
        )

        MyText(
            "OR",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        MyClickableSurface(
            color = AppTheme.colors.secondary,
            cornerRadius = 20.dp,
            modifier = Modifier.padding(horizontal = 40.dp),
            onClick = {
                fileLauncher.launch("*/*")
            }
        ) {
            MyColumn(
                modifier = Modifier.padding(10.dp)
            ) {
                if (st.importedFileName.isNotEmpty()) {
                    MyText(
                        st.importedFileName,
                        fontWeight = FontWeight.Bold
                    )
                }

                Icon(
                    painter = painterResource(R.drawable.ic_import),
                    contentDescription = stringResource(R.string.import_file),
                    tint = AppTheme.colors.text,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(vertical = 10.dp)
                )

                MyText(
                    stringResource(R.string.import_file),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        PrimaryPillBtn(
            text = stringResource(
                if (st.operation == Operation.ENCRYPT) R.string.encrypt
                else R.string.decrypt
            ),
            padding = PaddingValues(top = 50.dp, bottom = 20.dp),
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
                    )
                }

                CopyBtn(onClick = vm::onCopyResult)
            }
        }

        KeyPickerDlg(
            shown = st.keyPickerShown,
            algorithm = Algorithm.RSA,
            mainOnCancel = vm::onKeyPickerCancel,
            mainOnKeySelected = { vm.onKeySelected(it) },
        )

        RSAKeyGenDlg(
            shown = st.newKeyDialogShown,
            onCancel = vm::onNewKeyDlgCancel,
            onSubmit = { vm.onNewKeyDlgSubmit(it) }
        )
    }

    if (st.shouldShowFileSaved != 0) {
        LaunchedEffect(st.shouldShowFileSaved) {
            Toast.makeText(
                ctx,
                ctx.getString(R.string.file_saved),
                Toast.LENGTH_SHORT
            ).show()
        }
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
        modifier = Modifier.padding(vertical = 6.dp),
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

            MyRow(
                padding = PaddingValues(0.dp)
            ) {
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