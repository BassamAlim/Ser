package bassamalim.ser.features.keyStore

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import bassamalim.ser.R
import bassamalim.ser.core.models.StoreKey
import bassamalim.ser.core.ui.components.*
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun KeyStoreUI(
    nc: NavController = rememberAnimatedNavController(),
    vm: KeyStoreVM = hiltViewModel()
) {
    val st by vm.uiState.collectAsState()

    MyScaffold(
        title = stringResource(R.string.public_key_store)
    ) {
        if (st.loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                MyCircularProgressIndicator()
            }
        }
        else {
            MyColumn {
                MyRow {
                    if (st.keyPublished) {
                        SecondaryPillBtn(
                            text = stringResource(R.string.remove_my_key),
                            textColor = bassamalim.ser.core.ui.theme.Negative,
                            onClick = vm::onRemoveKey
                        )
                    }
                    else {
                        SecondaryPillBtn(
                            text = stringResource(R.string.publish_key),
                            onClick = vm::onPublishKey
                        )
                    }
                }

                MyHorizontalDivider(thickness = 2.dp)

                MyLazyColumn(
                    lazyList = {
                        itemsIndexed(st.items) { i, key ->
                            KeyItem(
                                key = key,
                                idx = i,
                                vm = vm
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun KeyItem(
    key: StoreKey,
    idx: Int,
    vm: KeyStoreVM
) {
    ExpandableItem(
        title = key.name,
        expandedContent = {
            KeySpace(
                titleResId = R.string.public_key,
                keyValue = key.value,
                onCopy = { vm.onCopyKey(idx) },
            )
        }
    )
}