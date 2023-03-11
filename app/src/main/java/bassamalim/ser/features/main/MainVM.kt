package bassamalim.ser.features.main

import androidx.lifecycle.ViewModel
import bassamalim.ser.features.main.MainRepo
import bassamalim.ser.features.main.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val repo: MainRepo
): ViewModel() {

    private val _uiState = MutableStateFlow(
        MainState(
        str = ""
    )
    )
    val uiState = _uiState.asStateFlow()

}