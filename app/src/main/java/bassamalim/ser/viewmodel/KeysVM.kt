package bassamalim.ser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import bassamalim.ser.repository.KeysRepo
import bassamalim.ser.state.KeysState
import bassamalim.ser.utils.Converter
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
            aesNameAlreadyExists = false,
        )}
    }

    fun onAESKeyAddDialogSubmit(name: String, value: String) {
        if (_uiState.value.aesNameAlreadyExists) return

        try {
            val bytes = Converter.decode(value)

            if (bytes.size != 16) throw java.lang.IllegalArgumentException()

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

    fun onAESKeyCopy(idx: Int) {
        Utils.copyToClipboard(
            app = app,
            text = _uiState.value.aesKeys[idx].asString(),
            label = "AES Key"
        )
    }

    fun onAESKeyRemove(idx: Int) {
        repo.removeAESKey(_uiState.value.aesKeys[idx].name)

        _uiState.update { it.copy(
            aesKeys = repo.getAESKeys()
        )}

        aesKeyNames = repo.getAESKeyNames()
    }

    fun onAESKeyRename(idx: Int) {

    }

    fun onAESKeySetPrimary(idx: Int) {

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
            rsaKeyAddDialogShown = false,
            rsaNameAlreadyExists = false,
            invalidRSAKey = false
        )}
    }

    fun onRSAKeyAddDialogSubmit(name: String, public: String, private: String) {
        if (_uiState.value.rsaNameAlreadyExists) return

        try {
            val publicBytes = Converter.decode(public)
            if (publicBytes.size != 62) throw java.lang.IllegalArgumentException()

            val privateBytes = Converter.decode(private)
            if (privateBytes.size < 196 || privateBytes.size > 198)
                throw java.lang.IllegalArgumentException()

            repo.insertRSAKey(name, public, private)

            _uiState.update { it.copy(
                rsaKeys = repo.getRSAKeys(),
                rsaKeyAddDialogShown = false
            )}
            rsaKeyNames = repo.getRSAKeyNames()

        } catch (e: java.lang.IllegalArgumentException) {
            _uiState.update { it.copy(
                invalidRSAKey = true
            )}
        }
    }

    fun onRSAPublicKeyCopy(idx: Int) {
        Utils.copyToClipboard(
            app = app,
            text = _uiState.value.rsaKeys[idx].key.publicAsString(),
            label = "RSA Public Key"
        )
    }

    fun onRSAPrivateKeyCopy(idx: Int) {
        Utils.copyToClipboard(
            app = app,
            text = _uiState.value.rsaKeys[idx].key.privateAsString(),
            label = "RSA Private Key"
        )
    }

    fun onRSAKeyRemove(idx: Int) {
        repo.removeRSAKey(_uiState.value.rsaKeys[idx].name)

        _uiState.update { it.copy(
            rsaKeys = repo.getRSAKeys()
        )}

        rsaKeyNames = repo.getRSAKeyNames()
    }

    fun onRSAKeyRename(idx: Int) {

    }

    fun onRSAKeySetPrimary(idx: Int) {

    }

}