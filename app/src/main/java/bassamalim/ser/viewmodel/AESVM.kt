package bassamalim.ser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import bassamalim.ser.data.Prefs
import bassamalim.ser.enums.Operation
import bassamalim.ser.helpers.Cryptography
import bassamalim.ser.models.AESKey
import bassamalim.ser.repository.AESRepo
import bassamalim.ser.state.AESState
import bassamalim.ser.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AESVM @Inject constructor(
    private val app: Application,
    private val repo: AESRepo
): AndroidViewModel(app) {

    private var text = ""
    private var key = repo.getKey(Prefs.SelectedAESKey.default as String)
    var keyNames = repo.getKeyNames()
        private set

    private val _uiState = MutableStateFlow(AESState(
        key = AESKey(
            name = Prefs.SelectedAESKey.default as String,
            value = Utils.encode(key)
        )
    ))
    val uiState = _uiState.asStateFlow()

    fun onCopyKey() {
        Utils.copyToClipboard(
            app = app,
            text = uiState.value.key.value,
            label = "AES Key"
        )
    }

    fun onSelectKey() {
        _uiState.update { it.copy(
            keyPickerShown = true
        )}
    }

    fun onKeySelected(idx: Int) {
        val name = keyNames[idx]

        key = repo.getKey(name)

        _uiState.update { it.copy(
            key = AESKey(
                name = name,
                value = Utils.encode(key)
            ),
            keyPickerShown = false
        )}

        repo.setSelectedKey(name)
    }

    fun onKeyPickerCancel() {
        _uiState.update { it.copy(
            keyPickerShown = false
        )}
    }

    fun onNewKey() {
        _uiState.update { it.copy(
            newKeyDialogShown = true
        )}
    }

    fun onNewKeyDlgNameCh(name: String) {
        _uiState.update { it.copy(
            nameAlreadyExists = keyNames.any { n -> n == name }
        )}
    }

    fun onNewKeyDlgSubmit(name: String) {
        if (uiState.value.nameAlreadyExists) return

        key = Cryptography.generateAESKey()

        repo.storeKey(name, key)

        _uiState.update { it.copy(
            newKeyDialogShown = false,
            key = AESKey(
                name = name,
                value = Utils.encode(key)
            )
        )}

        repo.setSelectedKey(name)

        keyNames = repo.getKeyNames()
    }

    fun onNewKeyDlgCancel() {
        _uiState.update { it.copy(
            newKeyDialogShown = false,
            nameAlreadyExists = false
        )}
    }

    fun onImportKey() {
        // TODO
    }

    fun onOpSwitch(isDecrypt: Boolean) {
        _uiState.update { it.copy(
            operation =
                if (isDecrypt) Operation.DECRYPT
                else Operation.ENCRYPT
        )}
    }

    fun onTextChange(text: String) {
        this.text = text
    }

    fun onExecute() {
        if (text.isEmpty()) return

        val result =
            if (uiState.value.operation == Operation.ENCRYPT) Cryptography.encryptAES(text, key)
            else Cryptography.decryptAES(text, key)

        _uiState.update { it.copy(
            result = result?.trim() ?: "Failed to ${uiState.value.operation.name.lowercase()}"
        )}
    }

    fun onCopyResult() {
        Utils.copyToClipboard(
            app = app,
            text = uiState.value.result,
            label = "AES Result"
        )
    }

}