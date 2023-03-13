package bassamalim.ser.features.rsaKeyGen

import androidx.lifecycle.ViewModel
import bassamalim.ser.core.helpers.Cryptography
import bassamalim.ser.core.models.MyKeyPair
import bassamalim.ser.core.models.RSAKeyPair
import bassamalim.ser.core.utils.Converter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RSAKeyGenVM @Inject constructor(
    private val repo: RSAKeyGenRepo
): ViewModel() {

    private val keyNames = repo.getKeyNames()
    var name = ""
        private set
    private var publicVal = ""
    private var privateVal = ""

    private val _uiState = MutableStateFlow(RSAKeyGenState())
    val uiState = _uiState.asStateFlow()

    fun onNameChange(name: String) {
        this.name = name
        _uiState.update { it.copy(
            nameExists = keyNames.contains(name)
        )}
    }

    fun onPublicChange(public: String) {
        this.publicVal = public
    }

    fun onPrivateChange(private: String) {
        this.privateVal = private
    }

    fun onGenerateCheckChange(checked: Boolean) {
        _uiState.update { it.copy(
            generateChecked = checked,
            valueInvalid = false
        )}
    }

    fun onSubmit(mainOnSubmit: (RSAKeyPair) -> Unit) {
        if (_uiState.value.nameExists || name.isEmpty() || _uiState.value.valueInvalid)
            return

        val genKey =
            if (_uiState.value.generateChecked) Cryptography.generateRSAKey()
            else null

        val public =
            if (_uiState.value.generateChecked) genKey!!.public
            else Converter.toPublicKey(publicVal)
        val private =
            if (_uiState.value.generateChecked) genKey!!.private
            else Converter.toPrivateKey(privateVal)

        val key = RSAKeyPair(
            name = name,
            key = MyKeyPair(public, private)
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