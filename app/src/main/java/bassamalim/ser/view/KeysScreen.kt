package bassamalim.ser.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

    MyCenterColumn {
        PrimaryPillBtn(text = stringResource(R.string.public_key_repo)) {

        }

        MyHorizontalDivider()

        MyCenterColumn {
            CategoryTitle(R.string.aes_keys)

            MyLazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .background(AppTheme.colors.secondary),
                lazyList = {
                    itemsIndexed(st.aesKeys) { i, key ->
                        MyCenterRow {
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

            CategoryTitle(R.string.rsa_keys)

            MyLazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 10.dp)
                    .background(AppTheme.colors.secondary),
                lazyList = {
                    itemsIndexed(st.rsaKeys) { i, key ->
                        MyCenterRow {
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
}
