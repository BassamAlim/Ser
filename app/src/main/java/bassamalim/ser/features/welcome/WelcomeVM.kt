package bassamalim.ser.features.welcome

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import bassamalim.ser.features.welcome.WelcomeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeVM @Inject constructor(
    private val repo: WelcomeRepo
): ViewModel() {

    val pref = repo.pref

    fun save(nc: NavController) {
        repo.unsetFirstTime()
    }

}