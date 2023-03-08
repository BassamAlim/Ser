package bassamalim.ser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import bassamalim.ser.data.Prefs
import bassamalim.ser.enums.Operation
import bassamalim.ser.helpers.Cryptography
import bassamalim.ser.models.RSAKeyPair
import bassamalim.ser.repository.RSARepo
import bassamalim.ser.state.RSAState
import bassamalim.ser.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RSAVM @Inject constructor(
    private val app: Application,
    private val repo: RSARepo
): AndroidViewModel(app) {

    private var text = ""
    private var keyPair = repo.getKey(Prefs.SelectedRSAKey.default as String)
    var keyNames = repo.getKeyNames()
        private set

    private val _uiState = MutableStateFlow(RSAState(
        keyPair = RSAKeyPair(
            name = Prefs.SelectedRSAKey.default as String,
            public = Utils.encode(keyPair.public),
            private = Utils.encode(keyPair.private)
        )
    ))
    val uiState = _uiState.asStateFlow()

    fun onCopyPublicKey() {
        Utils.copyToClipboard(
            app = app,
            text = uiState.value.keyPair.public,
            label = "RSA Public Key"
        )
    }

    fun onCopyPrivateKey() {
        Utils.copyToClipboard(
            app = app,
            text = uiState.value.keyPair.private,
            label = "RSA Private Key"
        )
    }

    fun onSelectKey() {
        _uiState.update { it.copy(
            keyPickerShown = true
        )}
    }

    fun onKeySelected(idx: Int) {
        val name = keyNames[idx]

        repo.setSelectedKey(name)

        keyPair = repo.getKey(name)

        _uiState.update { it.copy(
            keyPair = RSAKeyPair(
                name = name,
                public = Utils.encode(keyPair.public),
                private = Utils.encode(keyPair.private)
            ),
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

    fun onNewKeyDlgNameCh(name: String) {
        _uiState.update { it.copy(
            nameAlreadyExists = keyNames.any { n -> n == name }
        )}
    }

    fun onSaveDlgSubmit(name: String) {
        if (uiState.value.nameAlreadyExists) return

        keyPair = Cryptography.generateRSAKey()

        repo.storeKey(name, keyPair)

        repo.setSelectedKey(name)

        _uiState.update { it.copy(
            newKeyDialogShown = false,
            keyPair = RSAKeyPair(
                name = name,
                public = Utils.encode(keyPair.public),
                private = Utils.encode(keyPair.private)
            )
        )}

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
            if (uiState.value.operation == Operation.ENCRYPT)
                Cryptography.encryptRSA(text, keyPair.public)
            else
                Cryptography.decryptRSA(text, keyPair.private)

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