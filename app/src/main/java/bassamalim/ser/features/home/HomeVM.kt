package bassamalim.ser.features.home

import androidx.lifecycle.ViewModel
import bassamalim.ser.features.home.HomeRepo
import bassamalim.ser.features.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(
    private val repo: HomeRepo
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    fun onStart() {

    }

    fun onStop() {

    }

}