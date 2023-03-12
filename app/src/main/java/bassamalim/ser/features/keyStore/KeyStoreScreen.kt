package bassamalim.ser.features.keyStore

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun KeyStoreUI(
    nc: NavController = rememberAnimatedNavController(),
    vm: KeyStoreVM = hiltViewModel()
) {
    val st by vm.uiState.collectAsState()


}