package bassamalim.ser.features.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import bassamalim.ser.features.settings.SettingsRepo
import bassamalim.ser.features.settings.SettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsVM @Inject constructor(
    private val app: Application,
    private val repo: SettingsRepo
): AndroidViewModel(app) {

    val sp = repo.sp

    private val _uiState = MutableStateFlow(SettingsState())
    val uiState = _uiState.asStateFlow()

}