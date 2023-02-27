package bassamalim.ser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import bassamalim.ser.enums.Operation
import bassamalim.ser.helpers.Cryptography
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
    private var key = repo.getTempKey()
    var keyNames = repo.getKeyNames()
        private set

    private val _uiState = MutableStateFlow(AESState(
        keyAvailable = key != null
    ))
    val uiState = _uiState.asStateFlow()

    init {
        if (key != null) {
            _uiState.update { it.copy(
                key = Utils.encode(key!!)
            )}
        }
    }

    fun onCopyKey() {
        Utils.copyToClipboard(
            app = app,
            text = uiState.value.key,
            label = "AES Key"
        )
    }

    fun onSaveKey() {
        _uiState.update { it.copy(
            saveDialogShown = true
        )}
    }

    fun onSaveDialogNameChange(name: String) {
        _uiState.update { it.copy(
            nameAlreadyExists = keyNames.any { n -> n == name }
        )}
    }

    fun onSaveDialogSubmit(name: String) {
        if (uiState.value.nameAlreadyExists) return

        _uiState.update { it.copy(
            saveDialogShown = false
        )}

        repo.storeKey(name, key!!)

        keyNames = repo.getKeyNames()
    }

    fun onSaveDialogCancel() {
        _uiState.update { it.copy(
            saveDialogShown = false
        )}
    }

    fun onSelectKey() {
        _uiState.update { it.copy(
            keyPickerShown = true
        )}
    }

    fun onKeyPickerCancel() {
        _uiState.update { it.copy(
            keyPickerShown = false,
            nameAlreadyExists = false
        )}
    }

    fun onKeySelected(idx: Int) {
        key = repo.getKey(keyNames[idx])

        repo.storeTempKey(key!!)

        _uiState.update { it.copy(
            keyAvailable = true,
            key = Utils.encode(key!!),
            keyPickerShown = false
        )}
    }

    fun onGenerateKey() {
        key = Cryptography.generateAESKey()

        repo.storeTempKey(key!!)

        _uiState.update { it.copy(
            keyAvailable = true,
            key = Utils.encode(key!!)
        )}
    }

    fun onImportKey() {

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
        if (text.isEmpty() || key == null) return

        val result =
            if (uiState.value.operation == Operation.ENCRYPT) Cryptography.encryptAES(text, key!!)
            else Cryptography.decryptAES(text, key!!)

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