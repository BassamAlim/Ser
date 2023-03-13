package bassamalim.ser.features.rsa

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import bassamalim.ser.core.data.Prefs
import bassamalim.ser.core.enums.Operation
import bassamalim.ser.core.helpers.Cryptography
import bassamalim.ser.core.models.RSAKeyPair
import bassamalim.ser.core.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RSAVM @Inject constructor(
    private val app: Application,
    repo: RSARepo
): AndroidViewModel(app) {

    private var text = ""
    private var keyPair = repo.getKey(Prefs.SelectedRSAKeyName.default as String)

    private val _uiState = MutableStateFlow(RSAState())
    val uiState = _uiState.asStateFlow()

    fun onStart() {
        _uiState.update { it.copy(
            keyName = keyPair.name,
            publicKey = keyPair.key.publicAsString(),
            privateKey = keyPair.key.privateAsString()
        )}
    }

    fun onCopyPublicKey() {
        Utils.copyToClipboard(
            app = app,
            text = uiState.value.publicKey,
            label = "RSA Public Key"
        )
    }

    fun onCopyPrivateKey() {
        Utils.copyToClipboard(
            app = app,
            text = uiState.value.privateKey,
            label = "RSA Private Key"
        )
    }

    fun onSelectKey() {
        _uiState.update { it.copy(
            keyPickerShown = true
        )}
    }

    fun onKeySelected(selectedKeyPair: RSAKeyPair) {
        keyPair = selectedKeyPair

        _uiState.update { it.copy(
            keyName = keyPair.name,
            publicKey = keyPair.key.publicAsString(),
            privateKey = keyPair.key.privateAsString(),
            keyPickerShown = false
        )}
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

    fun onNewKeyDlgSubmit(newKeyPair: RSAKeyPair) {
        keyPair = newKeyPair

        _uiState.update { it.copy(
            keyName = keyPair.name,
            publicKey = keyPair.key.publicAsString(),
            privateKey = keyPair.key.privateAsString(),
            newKeyDialogShown = false
        )}
    }

    fun onNewKeyDlgCancel() {
        _uiState.update { it.copy(
            newKeyDialogShown = false
        )}
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
                Cryptography.encryptRSA(text, keyPair.key.public)
            else
                Cryptography.decryptRSA(text, keyPair.key.private)

        _uiState.update { it.copy(
            result = result?.trim() ?: "Failed to ${uiState.value.operation.name.lowercase()}"
        )}
    }

    fun onCopyResult() {
        Utils.copyToClipboard(
            app = app,
            text = uiState.value.result,
            label = "RSA Result"
        )
    }

}