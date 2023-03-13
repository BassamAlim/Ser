package bassamalim.ser.features.aesKeyGen

import androidx.compose.foundation.layout.*
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
import bassamalim.ser.core.models.AESKey
import bassamalim.ser.core.ui.components.*
import bassamalim.ser.core.ui.theme.Negative

@Composable
fun AESKeyGenDlg(
    vm: AESKeyGenVM = hiltViewModel(),
    shown: Boolean,
    onCancel: () -> Unit,
    mainOnSubmit: (AESKey) -> Unit
) {
    val st by vm.uiState.collectAsState()

    MyDialog(
        shown = shown,
        onDismiss = onCancel
    ) {
        MyColumn(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
        ) {
            DialogTitle(
                textResId = R.string.new_aes_key,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            MyColumn(
                modifier = Modifier
                    .height(260.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                MyOutlinedTextField(
                    label = stringResource(R.string.key_name),
                    onValueChange = { vm.onNameChange(it) }
                )

                if (st.nameExists) {
                    MyText(
                        text = stringResource(R.string.key_name_exists),
                        textColor = Negative
                    )
                }

                MyColumn {
                    MyRow(
                        arrangement = Arrangement.Start
                    ) {
                        MyCheckbox(
                            isChecked = st.generateChecked,
                            onCheckedChange = { vm.onGenerateCheckChange(it) }
                        )

                        MyText(stringResource(R.string.generate))
                    }

                    MyOutlinedTextField(
                        label = stringResource(R.string.key_value),
                        isEnabled = !st.generateChecked,
                        padding = PaddingValues(4.dp),
                        onValueChange = { vm.onValueChange(it) }
                    )

                    if (st.valueInvalid) {
                        MyText(
                            text = stringResource(R.string.key_value_invalid),
                            textColor = Negative
                        )
                    }
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