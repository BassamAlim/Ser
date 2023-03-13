package bassamalim.ser.features.aesKeyGen

import androidx.lifecycle.ViewModel
import bassamalim.ser.core.helpers.Cryptography
import bassamalim.ser.core.models.AESKey
import bassamalim.ser.core.utils.Converter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AESKeyGenVM @Inject constructor(
    private val repo: AESKeyGenRepo
): ViewModel() {

    private val keyNames = repo.getKeyNames()
    private var name = ""
    private var value = ""

    private val _uiState = MutableStateFlow(AESKeyGenState())
    val uiState = _uiState.asStateFlow()

    fun onNameChange(name: String) {
        this.name = name
        _uiState.update { it.copy(
            nameExists = keyNames.contains(name)
        )}
    }

    fun onValueChange(value: String) {
        try {
            val bytes = Converter.decode(value)

            if (bytes.size != 16) throw java.lang.IllegalArgumentException()
        }
        catch (e: java.lang.IllegalArgumentException) {
            _uiState.update { it.copy(
                valueInvalid = true
            )}
        }
    }

    fun onGenerateCheckChange(checked: Boolean) {
        _uiState.update { it.copy(
            generateChecked = checked,
            valueInvalid = false
        )}
    }

    fun onSubmit(mainOnSubmit: (AESKey) -> Unit) {
        if (_uiState.value.nameExists || name.isEmpty() || _uiState.value.valueInvalid)
            return

        val secret =
            if (_uiState.value.generateChecked) Cryptography.generateAESKey()
            else {
                try {
                    val bytes = Converter.decode(value)

                    if (bytes.size != 16) throw java.lang.IllegalArgumentException()

                    Converter.toSecretKey(bytes)
                } catch (e: java.lang.IllegalArgumentException) {
                    _uiState.update { it.copy(
                        valueInvalid = true
                    )}
                    return
                }
            }

        val key = AESKey(
            name = name,
            secret = secret
        )

        repo.storeKey(key)
        repo.setSelectedKey(name)

        _uiState.update { it.copy(
            nameExists = false,
            valueInvalid = false
        )}

        mainOnSubmit(key)
    }
	
}