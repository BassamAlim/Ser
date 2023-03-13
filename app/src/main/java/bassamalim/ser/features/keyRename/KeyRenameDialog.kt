package bassamalim.ser.features.keyRename

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bassamalim.ser.R
import bassamalim.ser.core.enums.Algorithm
import bassamalim.ser.core.ui.components.*
import bassamalim.ser.core.ui.theme.Negative

@Composable
fun KeyRenameDlg(
    shown: Boolean,
    algorithm: Algorithm,
    oldName: String,
    vm: KeyRenameVM = hiltViewModel(),
    onCancel: () -> Unit,
    mainOnSubmit: (String) -> Unit
) {
    val st by vm.uiState.collectAsState()

    vm.init(algorithm, oldName)

    MyDialog(
        shown = shown,
        onDismiss = onCancel
    ) {
        MyColumn(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
        ) {
            DialogTitle(
                textResId =
                    if (algorithm == Algorithm.AES) R.string.rename_key
                    else R.string.rename_keypair,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            MyColumn(
                modifier = Modifier
                    .height(260.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                MyOutlinedTextField(
                    label = stringResource(R.string.new_name),
                    onValueChange = { vm.onNameChange(it) }
                )

                if (st.nameExists) {
                    MyText(
                        text = stringResource(R.string.key_name_exists),
                        textColor = Negative
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
                    onClick = { vm.onSubmit(mainOnSubmit) }
                )
            }
        }
    }
}