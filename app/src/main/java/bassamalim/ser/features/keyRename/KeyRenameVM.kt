package bassamalim.ser.features.keyRename

import androidx.lifecycle.ViewModel
import bassamalim.ser.core.enums.Algorithm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class KeyRenameVM @Inject constructor(
    private val repo: KeyRenameRepo
): ViewModel() {

    private lateinit var publishedKeyName: String
    private lateinit var algorithm: Algorithm
    private lateinit var oldName: String
    private lateinit var keyNames: List<String>
    private var newName = ""

    private val _uiState = MutableStateFlow(KeyRenameState())
    val uiState = _uiState.asStateFlow()

    fun init(algorithm: Algorithm, oldName: String) {
        this.algorithm = algorithm
        this.oldName = oldName

        keyNames =
            if (algorithm == Algorithm.AES) repo.getAESKeyNames()
            else repo.getRSAKeyNames()

        if (algorithm == Algorithm.RSA)
            publishedKeyName = repo.getPublishedKeyName()
    }

    fun onNameChange(name: String) {
        newName = name

        _uiState.update { it.copy(
            nameExists = keyNames.contains(name)
        )}
    }

    fun onSubmit(mainOnSubmit: (String) -> Unit) {
        if (_uiState.value.nameExists) return

        if (algorithm == Algorithm.AES) repo.renameAESKey(oldName, newName)
        else repo.renameRSAKey(oldName, newName)

        if (algorithm == Algorithm.RSA && oldName == publishedKeyName)
            repo.setPublishedKeyName(newName)

        mainOnSubmit(newName)
    }

}