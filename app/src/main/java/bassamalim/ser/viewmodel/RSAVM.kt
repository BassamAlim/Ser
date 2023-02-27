package bassamalim.ser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import bassamalim.ser.enums.Operation
import bassamalim.ser.helpers.Cryptography
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
    private var keyPair = repo.getTempKey()
    var keyNames = repo.getKeyNames()
        private set

    private val _uiState = MutableStateFlow(RSAState(
        keyAvailable = keyPair != null
    ))
    val uiState = _uiState.asStateFlow()

    init {
        if (keyPair != null) {
            _uiState.update { it.copy(
                publicKey = if (keyPair!!.public != null) Utils.encode(keyPair!!.public)
                else "null",
                privateKey = if (keyPair!!.private != null) Utils.encode(keyPair!!.private)
                else "null"
            )}
        }
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
        _uiState.update { it.copy(
            saveDialogShown = false
        )}

        repo.storeKey(name, keyPair!!)

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
            keyPickerShown = false
        )}
    }

    fun onKeySelected(idx: Int) {
        keyPair = repo.getKey(keyNames[idx])

        repo.storeTempKey(keyPair!!)

        _uiState.update { it.copy(
            keyAvailable = true,
            publicKey = Utils.encode(keyPair!!.public),
            privateKey = Utils.encode(keyPair!!.private),
            keyPickerShown = false
        )}
    }

    fun onGenerateKey() {
        keyPair = Cryptography.generateRSAKey()

        repo.storeTempKey(keyPair!!)

        _uiState.update { it.copy(
            keyAvailable = true,
            publicKey = Utils.encode(keyPair!!.public),
            privateKey = Utils.encode(keyPair!!.private),
        )}
    }

    fun onImportKey() {

    }

    fun onOpSwitch(b: Boolean) {
        _uiState.update { it.copy(
            operation =
                if (it.operation == Operation.ENCRYPT) Operation.DECRYPT
                else Operation.ENCRYPT
        )}
    }

    fun onTextChange(text: String) {
        this.text = text
    }

    fun onExecute() {
        if (text.isEmpty() || keyPair == null) return

        val result =
            if (uiState.value.operation == Operation.ENCRYPT)
                Cryptography.encryptRSA(text, keyPair!!.public)
            else
                Cryptography.decryptRSA(text, keyPair!!.private)

        _uiState.update { it.copy(
            result = result.trim()
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