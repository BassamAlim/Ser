package bassamalim.ser.features.keyPicker

import androidx.compose.foundation.layout.fillMaxWidth
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
    onCancel: () -> Unit,
    mainOnKeySelected: (Key) -> Unit,
) {
    val st by vm.uiState.collectAsState()

    vm.init(algorithm)

    MyDialog(
        shown = shown,
        onDismiss = onCancel
    ) {
        MyColumn(
            Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
        ) {
            DialogTitle(
                if (algorithm == Algorithm.AES) R.string.pick_key
                else R.string.pick_keypair
            )

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

            SecondaryPillBtn(
                text = stringResource(R.string.cancel),
                onClick = onCancel
            )
        }
    }
}