package bassamalim.ser.features.aes

import android.app.Application
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import bassamalim.ser.core.data.Prefs
import bassamalim.ser.core.enums.Operation
import bassamalim.ser.core.helpers.Cryptography
import bassamalim.ser.core.models.AESKey
import bassamalim.ser.core.utils.Converter
import bassamalim.ser.core.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


@HiltViewModel
class AESVM @Inject constructor(
    private val app: Application,
    repo: AESRepo
): AndroidViewModel(app) {

    private var text = ""
    private var key = repo.getKey(Prefs.SelectedAESKeyName.default as String)
    private var importedFile: File? = null

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
        importedFile = File("/storage/emulated/0/$path")

        _uiState.update { it.copy(
            importedFileName = importedFile?.name ?: ""
        )}
    }

    fun onExecute() {
        if (text.isEmpty() && importedFile == null) return

        if (uiState.value.operation == Operation.ENCRYPT) {
            if (importedFile == null) encryptText()
            else encryptFile()
        }
        else {
            if (importedFile == null) decryptText()
            else decryptFile()
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
        val bytes = importedFile!!.readBytes()

        val result = Cryptography.encryptAES(bytes, key.secret) ?: return

        val fileName = importedFile!!.name

        importedFile = null
        _uiState.update { it.copy(
            importedFileName = ""
        )}

        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "$fileName.AES"
        )
        val outputStream = FileOutputStream(file)
        outputStream.write(result)
        outputStream.close()

        _uiState.update { it.copy(
            shouldShowFileSaved = _uiState.value.shouldShowFileSaved + 1
        )}
    }

    private fun decryptFile() {
        val bytes = importedFile!!.readBytes()

        val result = Cryptography.decryptAES(bytes, key.secret) ?: return

        var fileName = importedFile!!.name
        if (fileName.endsWith(".AES"))
            fileName = fileName.substring(0, fileName.length - 4)

        importedFile = null
        _uiState.update { it.copy(
            importedFileName = ""
        )}

        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            fileName
        )
        val outputStream = FileOutputStream(file)
        outputStream.write(result)
        outputStream.close()

        _uiState.update { it.copy(
            shouldShowFileSaved = _uiState.value.shouldShowFileSaved + 1
        )}
    }

}