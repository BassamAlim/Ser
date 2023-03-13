package bassamalim.ser.features.keyStore

import android.annotation.SuppressLint
import android.app.Application
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import bassamalim.ser.core.models.StoreKey
import bassamalim.ser.core.utils.Utils
import com.google.firebase.Timestamp
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

    private val publishedKey = repo.getPublishedKey()

    private val _uiState = MutableStateFlow(KeyStoreState())
    val uiState = _uiState.asStateFlow()

    init {
        refreshItems()
    }

    @SuppressLint("HardwareIds")
    fun onPublishKey() {
        val deviceId = getDeviceId()

        if (_uiState.value.items.any { it.deviceId == deviceId }) return
        if (_uiState.value.items.any { it.name == publishedKey.name }) return

        viewModelScope.launch {
            repo.publishKey(
                StoreKey(
                    name = publishedKey.name,
                    value = publishedKey.key.publicAsString(),
                    deviceId = deviceId,
                    created = Timestamp.now()
                )
            )
        }

        refreshItems()
    }

    fun onRemoveKey() {
        val deviceId = getDeviceId()
        viewModelScope.launch {
            repo.removeKey(deviceId)
        }

        refreshItems()
    }

    fun onCopyKey(idx: Int) {
        Utils.copyToClipboard(
            app = app,
            text = _uiState.value.items[idx].value,
            label = "${_uiState.value.items[idx].name}'s RSA public key"
        )
    }

    private fun refreshItems() {
        val deviceId = getDeviceId()

        viewModelScope.launch {
            val items = repo.getKeys()
            _uiState.update { it.copy(
                items = items,
                keyPublished = items.any { key -> key.deviceId == deviceId },
                loading = false
            )}
        }
    }

    @SuppressLint("HardwareIds")
    private fun getDeviceId(): String {
        return Settings.Secure.getString(
            app.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

}