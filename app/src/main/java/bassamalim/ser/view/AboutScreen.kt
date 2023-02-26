package bassamalim.ser.view

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import bassamalim.ser.R
import bassamalim.ser.ui.components.MyScaffold
import bassamalim.ser.ui.components.MyText
import bassamalim.ser.viewmodel.AboutVM

@Composable
fun AboutUI(
    vm: AboutVM = hiltViewModel()
) {
    val st by vm.uiState.collectAsState()
    val ctx = LocalContext.current

    MyScaffold(stringResource(R.string.about)) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 5.dp)
        ) {
            MyText(
                text = stringResource(R.string.thanks),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 15.dp, bottom = 20.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        vm.onTitleClick()
                    }
            )
        }
    }

    if (st.shouldShowRebuilt != 0) {
        LaunchedEffect(key1 = st.shouldShowRebuilt) {
            Toast.makeText(
                ctx, ctx.getString(R.string.database_rebuilt),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}