package bassamalim.ser.features.keyPicker

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bassamalim.ser.R
import bassamalim.ser.core.enums.Algorithm
import bassamalim.ser.core.models.Key
import bassamalim.ser.core.ui.components.*

@Composable
fun KeyPickerDlg(
    shown: Boolean,
    algorithm: Algorithm,
    vm: KeyPickerVM = hiltViewModel(),
    mainOnCancel: () -> Unit,
    mainOnKeySelected: (Key) -> Unit,
) {
    val st by vm.uiState.collectAsState()

    vm.init(algorithm)

    MyDialog(
        shown = shown,
        onDismiss = { vm.onCancel(mainOnCancel) }
    ) {
        MyColumn(
            modifier = Modifier.padding(vertical = 10.dp)
        ) {
            DialogTitle(R.string.pick_key)

            MyColumn(
                Modifier.height(300.dp)
            ) {
                if (st.loading) Loading()
                else {
                    MyColumn(
                        Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
                    ) {
                        if (algorithm == Algorithm.RSA) {
                            MyCheckbox(
                                text = stringResource(R.string.from_key_store),
                                initialState = st.fromKeyStore,
                                onCheckedChange = { vm.onFromKeyStoreCheckedCh(it) },
                            )
                        }

                        MyLazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 30.dp),
                            lazyList = {
                                itemsIndexed(st.items) { i, key ->
                                    MyClickableText(
                                        text = key.name,
                                        onClick = { vm.onKeySelected(key, mainOnKeySelected) }
                                    )

                                    if (i != st.items.lastIndex) MyHorizontalDivider()
                                }
                            }
                        )
                    }
                }
            }

            SecondaryPillBtn(
                text = stringResource(R.string.cancel),
                onClick = { vm.onCancel(mainOnCancel) }
            )
        }
    }
}