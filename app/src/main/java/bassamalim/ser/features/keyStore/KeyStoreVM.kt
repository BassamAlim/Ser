package bassamalim.ser.features.keyStore

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import bassamalim.ser.core.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KeyStoreVM @Inject constructor(
    private val app: Application,
    private val repo: KeyStoreRepo
): AndroidViewModel(app) {

    private val deviceId = Utils.getDeviceId(app)

    private val _uiState = MutableStateFlow(KeyStoreState())
    val uiState = _uiState.asStateFlow()

    init {
        val publishedKeyName = repo.getPublishedKeyName()

        var deviceRegistered = true
        viewModelScope.launch {
            deviceRegistered = repo.deviceRegistered(deviceId)
        }
        if (publishedKeyName.isEmpty() && deviceRegistered)
            removeOldKey()

        viewModelScope.launch {
            _uiState.update { it.copy(
                keyPublished = publishedKeyName.isNotEmpty(),
                userName = repo.getUserName(),
                publishedKeyName = publishedKeyName,
                items = repo.getKeys(),
                loading = false
            )}
        }
    }

    fun onPublishKey() {
        _uiState.update { it.copy(
            keyPublisherShown = true
        )}
    }

    fun onKeyPublisherCancel() {
        _uiState.update { it.copy(
            keyPublisherShown = false
        )}
    }

    fun onKeyPublisherSubmit(userName: String, keyName: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(
                keyPublished = true,
                userName = userName,
                publishedKeyName = keyName,
                items = repo.getKeys(),
                keyPublisherShown = false
            )}
        }
    }

    fun onRemoveKey() {
        viewModelScope.launch {
            repo.removeKey(deviceId)

            repo.setPublishedKeyName("")

            _uiState.update { it.copy(
                keyPublished = false,
                publishedKeyName = "",
                items = repo.getKeys()
            )}
        }
    }

    fun onCopyKey(idx: Int) {
        Utils.copyToClipboard(
            app = app,
            text = _uiState.value.items[idx].public,
            label = "${_uiState.value.items[idx].name}'s RSA public key"
        )
    }

    private fun removeOldKey() {
        viewModelScope.launch {
            repo.removeKey(deviceId)
        }
    }

}