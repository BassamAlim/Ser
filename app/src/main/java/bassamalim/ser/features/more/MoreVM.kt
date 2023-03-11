package bassamalim.ser.features.more

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import bassamalim.ser.core.nav.Screen
import bassamalim.ser.core.data.GlobalVals
import bassamalim.ser.features.more.MoreState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MoreVM @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(MoreState())
    val uiState = _uiState.asStateFlow()

    /*fun goto###(nc: NavController) {
        nc.navigate(Screen.###.route)
    }*/

    /*fun goto###(nc: NavController) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            nc.navigate(Screen.###.route)
        else
            _uiState.update { it.copy(
                shouldShowUnsupported = true
            )}
    }*/

    fun gotoSettings(nc: NavController) {
        nc.navigate(Screen.Settings.route)
    }

    fun contactMe(ctx: Context) {
        val contactIntent = Intent(
            Intent.ACTION_SENDTO,
            Uri.fromParts("mailto", GlobalVals.CONTACT_EMAIL, null)
        )
        contactIntent.putExtra(Intent.EXTRA_SUBJECT, "Hidaya")
        contactIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        ctx.startActivity(
            Intent.createChooser(
                contactIntent,
                "Choose an Email client :"
            )
        )
    }

    fun shareApp(ctx: Context) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "App Share")
        sharingIntent.putExtra(Intent.EXTRA_TEXT, GlobalVals.PLAY_STORE_URL)
        ctx.startActivity(
            Intent.createChooser(sharingIntent, "Share via")
        )
    }

    fun gotoAbout(nc: NavController) {
        nc.navigate(Screen.About.route)
    }

}