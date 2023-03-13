package bassamalim.ser.features.keyPublisher

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import bassamalim.ser.core.models.StoreKey
import bassamalim.ser.core.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KeyPublisherVM @Inject constructor(
    app: Application,
    private val repo: KeyPublisherRepo
): AndroidViewModel(app) {

    private val deviceId = Utils.getDeviceId(app)
    private val keyNames = repo.getKeyNames()
    var userName = repo.getUserName()
        private set
    private var key = repo.getKey(keyNames[0])

    private val _uiState = MutableStateFlow(KeyPublisherState(
        keyNames = keyNames
    ))
    val uiState = _uiState.asStateFlow()

	fun onNameChange(name: String) {
        userName = name

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                nameExists = repo.nameExists(name)
            )
        }
    }

    fun onKeyChoice(idx: Int) {
        key = repo.getKey(keyNames[idx])
    }

    fun onSubmit(mainOnSubmit: (String, String) -> Unit) {
        if (userName.length < 3 || _uiState.value.nameExists) return

        _uiState.update { it.copy(
            loading = true
        )}

        viewModelScope.launch {
            repo.publishKey(
                StoreKey(
                    name = userName,
                    public = key.key.publicAsString()
                ),
                deviceId
            )
        }

        repo.setUserName(userName)
        repo.setPublishedKeyName(key.name)

        mainOnSubmit(userName, key.name)
    }

}