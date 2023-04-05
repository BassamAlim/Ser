package bassamalim.ser.features.rsa

import android.app.Application
import android.net.Uri
import androidx.compose.foundation.ScrollState
import androidx.lifecycle.AndroidViewModel
import bassamalim.ser.core.data.Prefs
import bassamalim.ser.core.enums.Operation
import bassamalim.ser.core.helpers.Cryptography
import bassamalim.ser.core.models.Key
import bassamalim.ser.core.models.RSAKeyPair
import bassamalim.ser.core.models.StoreKey
import bassamalim.ser.core.utils.Converter
import bassamalim.ser.core.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.security.PublicKey
import javax.inject.Inject

@HiltViewModel
class RSAVM @Inject constructor(
    private val app: Application,
    repo: RSARepo
): AndroidViewModel(app) {

    private lateinit var coroutineScope: CoroutineScope
    private lateinit var scrollState: ScrollState
    private var text = ""
    private var keyPair = repo.getKey(Prefs.SelectedRSAKeyName.default as String)
    private var publicStoreKey: PublicKey? = null
    private var importedFileBytes: ByteArray? = null

    private val _uiState = MutableStateFlow(RSAState())
    val uiState = _uiState.asStateFlow()

    fun onStart(coroutineScope: CoroutineScope, scrollState: ScrollState) {
        this.coroutineScope = coroutineScope
        this.scrollState = scrollState

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

    fun onKeySelected(selectedKeyPair: Key) {
        if (selectedKeyPair is StoreKey) {
            publicStoreKey = selectedKeyPair.asPublicKey()

            _uiState.update { it.copy(
                storeKey = true,
                keyName = selectedKeyPair.name,
                publicKey = selectedKeyPair.public,
                privateKey = "Unavailable",
                keyPickerShown = false
            )}
        }
        else {
            keyPair = selectedKeyPair as RSAKeyPair

            _uiState.update { it.copy(
                keyName = keyPair.name,
                publicKey = keyPair.key.publicAsString(),
                privateKey = keyPair.key.privateAsString(),
                keyPickerShown = false
            )}
        }
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

    fun onFileImportResult(uri: Uri?) {
        if (uri == null) return

        val path = uri.path!!.split(":").last()
        val file = File("/storage/emulated/0/$path")
        _uiState.update { it.copy(
            importedFileName = file.name ?: ""
        )}

        importedFileBytes = Converter.uriToByteArray(uri, app.contentResolver)
    }

    fun onCopyResult() {
        Utils.copyToClipboard(
            app = app,
            text = uiState.value.result,
            label = "RSA Result"
        )
    }

    fun onExecute() {
        if (text.isEmpty() && importedFileBytes == null) return

        if (uiState.value.operation == Operation.ENCRYPT) {
            if (importedFileBytes == null) encryptText()
            else encryptFile()

            coroutineScope.launch {
                scrollState.animateScrollTo(Int.MAX_VALUE)
            }
        }
        else {
            if (_uiState.value.storeKey) return  // cannot decrypt with store key

            if (importedFileBytes == null) decryptText()
            else decryptFile()

            coroutineScope.launch {
                scrollState.animateScrollTo(Int.MAX_VALUE)
            }
        }
    }

    private fun encryptText() {
        val bytes = text.toByteArray()

        val result = Cryptography.encryptRSA(bytes, keyPair.key.public)
        _uiState.update { it.copy(
            result =
                if (result == null) "Failed to encrypt"
                else Converter.encode(result)
        )}
    }

    private fun decryptText() {
        val bytes = Converter.decode(text)

        val result = Cryptography.decryptRSA(bytes, keyPair.key.private)
        _uiState.update { it.copy(
            result = result?.decodeToString() ?: "Failed to decrypt"
        )}
    }

    private fun encryptFile() {
        val result = Cryptography.encryptRSA(importedFileBytes!!, keyPair.key.public) ?: return

        val extension = _uiState.value.importedFileName.split(".").last()
        val fName = _uiState.value.importedFileName
            .removeSuffix(extension)
            .removeSuffix(".")

        try {
            Utils.writeToDownloads(
                filename = "RSA Encrypted {$fName}.$extension",
                bytes = result
            )
        } catch (e: Exception) {
            _uiState.update { it.copy(
                result = "Failed to save file"
            )}
        }

        _uiState.update { it.copy(
            importedFileName = "",
            shouldShowFileSaved = _uiState.value.shouldShowFileSaved + 1
        )}
    }

    private fun decryptFile() {
        val result = Cryptography.decryptRSA(importedFileBytes!!, keyPair.key.private) ?: return

        var fName = _uiState.value.importedFileName
            .removeSuffix(".")
            .replaceFirst("Encrypted {", "Decrypted {")
            .replaceFirst("AES", "RSA")
        if (fName.startsWith("AES Encrypted {")) {
            fName = fName.replaceFirst("AES", "RSA")
        }

        try {
            Utils.writeToDownloads(
                filename = fName,
                bytes = result
            )
        } catch (e: Exception) {
            _uiState.update { it.copy(
                result = "Failed to save file"
            )}
        }

        _uiState.update { it.copy(
            importedFileName = "",
            shouldShowFileSaved = _uiState.value.shouldShowFileSaved + 1
        )}
    }

}