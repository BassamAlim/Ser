package bassamalim.ser.features.keyPicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bassamalim.ser.core.enums.Algorithm
import bassamalim.ser.core.models.Key
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    private fun getStoreKeys() {
        _uiState.update { it.copy(
            loading = true
        )}

        viewModelScope.launch {
            _uiState.update { it.copy(
                items = repo.getStoreKeys(),
                loading = false
            )}
        }
    }

    fun onFromKeyStoreCheckedCh(checked: Boolean) {
        _uiState.update { it.copy(
            fromKeyStore = checked
        )}

        if (checked) getStoreKeys()
        else getKeys()
    }

    fun onKeySelected(key: Key, mainOnSelected: (Key) -> Unit) {
        if (algorithm == Algorithm.AES) repo.setAESSelectedKey(key.name)
        else repo.setRSASelectedKey(key.name)

        _uiState.update { it.copy(
            fromKeyStore = false
        )}

        mainOnSelected(key)
    }

    fun onCancel(mainOnCancel: () -> Unit) {
        _uiState.update { it.copy(
            fromKeyStore = false
        )}

        mainOnCancel()
    }

}