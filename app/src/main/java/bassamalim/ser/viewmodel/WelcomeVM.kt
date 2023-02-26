package bassamalim.ser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import bassamalim.ser.repository.WelcomeRepo
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