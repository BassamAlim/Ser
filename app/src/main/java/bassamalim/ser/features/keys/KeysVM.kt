package bassamalim.ser.features.keys

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import bassamalim.ser.core.enums.Algorithm
import bassamalim.ser.core.nav.Screen
import bassamalim.ser.core.utils.Utils
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
            rsaKeys = repo.getRSAKeys(),
            publishedKeyName = repo.getPublishedKeyName()
        )}
    }

    fun onPublicKeyStore(nc: NavController) {
        nc.navigate(
            Screen.KeyStore.route
        )
    }

    fun onAddAESKey() {
        _uiState.update { it.copy(
            aesKeyAddDialogShown = true
        )}
    }

    fun onAESKeyAddDialogCancel() {
        _uiState.update { it.copy(
            aesKeyAddDialogShown = false
        )}
    }

    fun onAESKeyAddDialogSubmit() {
        _uiState.update { it.copy(
            aesKeys = repo.getAESKeys(),
            aesKeyAddDialogShown = false
        )}

        aesKeyNames = repo.getAESKeyNames()
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
        _uiState.update { it.copy(
            keyRenameDialogShown = true,
            keyRenameDialogAlgorithm = Algorithm.AES,
            keyRenameDialogOldName = _uiState.value.aesKeys[idx].name
        )}
    }


    fun onAddRSAKey() {
        _uiState.update { it.copy(
            rsaKeyAddDialogShown = true
        )}
    }

    fun onRSAKeyAddDialogCancel() {
        _uiState.update { it.copy(
            rsaKeyAddDialogShown = false
        )}
    }

    fun onRSAKeyAddDialogSubmit() {
        _uiState.update { it.copy(
            rsaKeys = repo.getRSAKeys(),
            rsaKeyAddDialogShown = false
        )}

        rsaKeyNames = repo.getRSAKeyNames()
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
        _uiState.update { it.copy(
            keyRenameDialogShown = true,
            keyRenameDialogAlgorithm = Algorithm.RSA,
            keyRenameDialogOldName = _uiState.value.rsaKeys[idx].name
        )}
    }

    fun onKeyRenameDialogCancel() {
        _uiState.update { it.copy(
            keyRenameDialogShown = false
        )}
    }

    fun onKeyRenameDialogSubmit(newName: String) {
        if (_uiState.value.keyRenameDialogAlgorithm == Algorithm.AES) {
            aesKeyNames = repo.getAESKeyNames()

            _uiState.update { it.copy(
                aesKeys = repo.getAESKeys(),
                keyRenameDialogShown = false
            )}
        }
        else {
            rsaKeyNames = repo.getRSAKeyNames()

            _uiState.update { it.copy(
                rsaKeys = repo.getRSAKeys(),
                publishedKeyName = repo.getPublishedKeyName(),
                keyRenameDialogShown = false
            )}
        }
    }

}