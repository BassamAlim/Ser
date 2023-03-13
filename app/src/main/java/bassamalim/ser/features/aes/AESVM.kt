package bassamalim.ser.features.aes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import bassamalim.ser.core.data.Prefs
import bassamalim.ser.core.enums.Operation
import bassamalim.ser.core.helpers.Cryptography
import bassamalim.ser.core.models.AESKey
import bassamalim.ser.core.utils.Utils
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
    private var key = repo.getKey(Prefs.SelectedAESKeyName.default as String)
    var keyNames = repo.getKeyNames()
        private set

    private val _uiState = MutableStateFlow(AESState())
    val uiState = _uiState.asStateFlow()

    fun onStart() {
        _uiState.update { it.copy(
            keyName = key.name,
            secretKey = key.asString()
        )}
    }

    fun onCopyKey() {
        Utils.copyToClipboard(
            app = app,
            text = uiState.value.secretKey,
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
            keyName = key.name,
            secretKey = key.asString(),
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

    fun onNewKeyDlgSubmit(key: AESKey) {
        this.key = key

        _uiState.update { it.copy(
            keyName = key.name,
            secretKey = key.asString(),
            newKeyDialogShown = false
        )}

        keyNames = repo.getKeyNames()
    }

    fun onNewKeyDlgCancel() {
        _uiState.update { it.copy(
            newKeyDialogShown = false
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
            if (uiState.value.operation == Operation.ENCRYPT)
                Cryptography.encryptAES(text, key.secret)
            else
                Cryptography.decryptAES(text, key.secret)

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