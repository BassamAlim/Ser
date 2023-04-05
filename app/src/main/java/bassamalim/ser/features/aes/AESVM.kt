package bassamalim.ser.features.aes

import android.app.Application
import android.net.Uri
import androidx.compose.foundation.ScrollState
import androidx.lifecycle.AndroidViewModel
import bassamalim.ser.core.data.Prefs
import bassamalim.ser.core.enums.Operation
import bassamalim.ser.core.helpers.Cryptography
import bassamalim.ser.core.models.AESKey
import bassamalim.ser.core.utils.Converter
import bassamalim.ser.core.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AESVM @Inject constructor(
    private val app: Application,
    repo: AESRepo
): AndroidViewModel(app) {

    private lateinit var coroutineScope: CoroutineScope
    private lateinit var scrollState: ScrollState
    private var text = ""
    private var key = repo.getKey(Prefs.SelectedAESKeyName.default as String)
    private var importedFileBytes: ByteArray? = null

    private val _uiState = MutableStateFlow(AESState())
    val uiState = _uiState.asStateFlow()

    fun onStart(coroutineScope: CoroutineScope, scrollState: ScrollState) {
        this.coroutineScope = coroutineScope
        this.scrollState = scrollState

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

    fun onKeySelected(selectedKey: AESKey) {
        key = selectedKey

        _uiState.update { it.copy(
            keyName = key.name,
            secretKey = key.asString(),
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

    fun onNewKeyDlgSubmit(newKey: AESKey) {
        key = newKey

        _uiState.update { it.copy(
            keyName = newKey.name,
            secretKey = newKey.asString(),
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
            if (importedFileBytes == null) decryptText()
            else decryptFile()

            coroutineScope.launch {
                scrollState.animateScrollTo(Int.MAX_VALUE)
            }
        }
    }

    fun onCopyResult() {
        Utils.copyToClipboard(
            app = app,
            text = uiState.value.result,
            label = "AES Result"
        )
    }

    private fun encryptText() {
        val bytes = text.toByteArray()

        val result = Cryptography.encryptAES(bytes, key.secret)
        _uiState.update { it.copy(
            result =
                if (result == null) "Failed to encrypt"
                else Converter.encode(result)
        )}
    }

    private fun decryptText() {
        val bytes = Converter.decode(text)

        val result = Cryptography.decryptAES(bytes, key.secret)
        println("result $result")

        _uiState.update { it.copy(
            result = result?.decodeToString() ?: "Failed to decrypt"
        )}
    }

    private fun encryptFile() {
        val result = Cryptography.encryptAES(importedFileBytes!!, key.secret) ?: return

        val extension = _uiState.value.importedFileName.split(".").last()
        val fName = _uiState.value.importedFileName
            .removeSuffix(extension)
            .removeSuffix(".")

        try {
            Utils.writeToDownloads(
                filename = "AES Encrypted {$fName}.$extension",
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
        val result = Cryptography.decryptAES(importedFileBytes!!, key.secret) ?: return

        var fName = _uiState.value.importedFileName
            .removeSuffix(".")
            .replaceFirst("Encrypted {", "Decrypted {")
            .replaceFirst("RSA", "AES")
        if (fName.startsWith("RSA Encrypted {")) {
            fName = fName.replaceFirst("RSA", "AES")
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