package bassamalim.ser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import bassamalim.ser.repository.KeysRepo
import bassamalim.ser.state.KeysState
import bassamalim.ser.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class KeysVM @Inject constructor(
    private val app: Application,
    private val repo: KeysRepo
): AndroidViewModel(app) {

    private val _uiState = MutableStateFlow(KeysState())
    val uiState = _uiState.asStateFlow()

    fun onStart() {
        _uiState.update { it.copy(
            aesKeys = repo.getAESKeys(),
            rsaKeys = repo.getRSAKeys()
        ) }
    }

    fun onAESKeyClk(idx: Int) {

    }

    fun onAESKeyCopy(idx: Int) {
        Utils.copyToClipboard(
            app = app,
            text = _uiState.value.aesKeys[idx].key,
            label = "AES Key"
        )
    }


    fun onRSAKeyClk(idx: Int) {

    }

}