package bassamalim.ser.features.keyPublisher

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bassamalim.ser.R
import bassamalim.ser.core.ui.components.*
import bassamalim.ser.core.ui.theme.Negative

@Composable
fun KeyPublisherDlg(
    shown: Boolean,
    vm: KeyPublisherVM = hiltViewModel(),
    onCancel: () -> Unit,
    mainOnSubmit: (String, String) -> Unit
) {
    val st by vm.uiState.collectAsState()

    MyDialog(
        shown = shown,
        onDismiss = onCancel
    ) {
        if (st.loading) Loading()
        else {
            MyColumn(
                Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
            ) {
                DialogTitle(R.string.publish_key)

                MyOutlinedTextField(
                    label = stringResource(R.string.your_name),
                    initialValue = vm.userName,
                    onValueChange = { vm.onNameChange(it) }
                )

                if (st.nameExists) {
                    MyText(
                        text = stringResource(R.string.name_exists),
                        textColor = Negative
                    )
                }

                MyDropDownMenu(
                    items = st.keyNames,
                    onChoice = { vm.onKeyChoice(it) }
                )

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
                        text = stringResource(R.string.publish),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 5.dp),
                        onClick = { vm.onSubmit(mainOnSubmit) }
                    )
                }
            }
        }
    }
}