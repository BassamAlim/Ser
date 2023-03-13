package bassamalim.ser.features.keyPicker

import androidx.lifecycle.ViewModel
import bassamalim.ser.core.enums.Algorithm
import bassamalim.ser.core.models.Key
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class KeyPickerVM @Inject constructor(
    private val repo: KeyPickerRepo
): ViewModel() {

    private lateinit var algorithm: Algorithm

    private val _uiState = MutableStateFlow(KeyPickerState())
    val uiState = _uiState.asStateFlow()

    fun init(algorithm: Algorithm) {
        this.algorithm = algorithm

        getKeys()
    }

    private fun getKeys() {
        _uiState.update { it.copy(
            items =
                if (algorithm == Algorithm.AES) repo.getAESKeys()
                else repo.getRSAKeys()
        )}
    }

    fun onKeySelected(key: Key, mainOnSelected: (Key) -> Unit) {
        if (algorithm == Algorithm.AES) repo.setAESSelectedKey(key.name)
        else repo.setRSSelectedKey(key.name)

        mainOnSelected(key)
    }

}