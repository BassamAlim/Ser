package bassamalim.ser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import bassamalim.ser.repository.SettingsRepo
import bassamalim.ser.state.SettingsState
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