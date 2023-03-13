package bassamalim.ser.features.more

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import bassamalim.ser.R
import bassamalim.ser.core.ui.components.MySquareButton
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MoreUI(
    nc: NavController = rememberAnimatedNavController(),
    vm: MoreVM = hiltViewModel()
) {
    val st = vm.uiState.collectAsState()
    val ctx = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 5.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MySquareButton(
                textResId = R.string.contact,
                imageResId = R.drawable.ic_mail
            ) {
                vm.contactMe(ctx)
            }

            MySquareButton(
                textResId = R.string.about,
                imageResId = R.drawable.ic_info
            ) {
                vm.gotoAbout(nc)
            }
        }

        // TODO: enable after publishing
        /*Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MySquareButton(
                textResId = R.string.share_app,
                imageResId = R.drawable.ic_share
            ) {
                vm.shareApp(ctx)
            }
        }*/
    }

    if (st.value.shouldShowUnsupported) {
        LaunchedEffect(null) {
            Toast.makeText(
                ctx,
                ctx.getString(R.string.feature_not_supported),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}