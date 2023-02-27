package bassamalim.ser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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

    private var aesKeyNames = repo.getAESKeyNames()
    private var rsaKeyNames = repo.getRSAKeyNames()

    private val _uiState = MutableStateFlow(KeysState())
    val uiState = _uiState.asStateFlow()

    fun onStart() {
        _uiState.update { it.copy(
            aesKeys = repo.getAESKeys(),
            rsaKeys = repo.getRSAKeys()
        )}
    }

    fun onAddAESKey() {
        _uiState.update { it.copy(
            aesKeyAddDialogShown = true
        )}
    }

    fun onAESKeyAddDialogNameChange(name: String) {
        _uiState.update { it.copy(
            aesNameAlreadyExists = aesKeyNames.any { n -> n == name }
        )}
    }

    fun onAESKeyAddDialogCancel() {
        _uiState.update { it.copy(
            aesKeyAddDialogShown = false,
            invalidAESKey = false,
            invalidRSAKey = false,
            aesNameAlreadyExists = false,
            rsaNameAlreadyExists = false
        )}
    }

    fun onAESKeyAddDialogSubmit(name: String, value: String) {
        if (_uiState.value.aesNameAlreadyExists) return

        try {
            val bytes = Utils.decode(value)

            val valid = bytes.size == 16 || bytes.size == 24 || bytes.size == 32
            if (!valid) throw java.lang.IllegalArgumentException()

            repo.insertAESKey(name, value)

            _uiState.update { it.copy(
                aesKeys = repo.getAESKeys(),
                aesKeyAddDialogShown = false
            )}
            aesKeyNames = repo.getAESKeyNames()

        } catch (e: java.lang.IllegalArgumentException) {
            _uiState.update { it.copy(
                invalidAESKey = true
            )}
        }
    }

    fun onAddRSAKey() {
        _uiState.update { it.copy(
            rsaKeyAddDialogShown = true
        )}
    }

    fun onRSAKeyAddDialogNameChange(name: String) {
        _uiState.update { it.copy(
            rsaNameAlreadyExists = rsaKeyNames.any { n -> n == name }
        )}
    }

    fun onRSAKeyAddDialogCancel() {
        _uiState.update { it.copy(
            rsaKeyAddDialogShown = false
        )}
    }

    fun onRSAKeyAddDialogSubmit(name: String, public: String, private: String) {
        if (_uiState.value.rsaNameAlreadyExists) return

        rsaKeyNames = repo.getRSAKeyNames()

        _uiState.update { it.copy(
            rsaKeys = repo.getRSAKeys(),
            rsaKeyAddDialogShown = false
        )}

        repo.insertRSAKey(name, public, private)
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