package bassamalim.ser.features.keyStore

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class KeyStoreVM @Inject constructor(
    private val repo: KeyStoreRepo
): ViewModel() {

    private val _uiState = MutableStateFlow(KeyStoreState())
    val uiState = _uiState.asStateFlow()

}