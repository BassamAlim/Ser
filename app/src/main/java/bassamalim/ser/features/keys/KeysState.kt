package bassamalim.ser.features.keys

import bassamalim.ser.core.enums.Algorithm
import bassamalim.ser.core.models.AESKey
import bassamalim.ser.core.models.RSAKeyPair

data class KeysState(
    val aesKeys: List<AESKey> = emptyList(),
    val rsaKeys: List<RSAKeyPair> = emptyList(),
    val publishedKeyName: String = "",
    val aesKeyAddDialogShown: Boolean = false,
    val rsaKeyAddDialogShown: Boolean = false,
    val keyRenameDialogShown: Boolean = false,
    val keyRenameDialogAlgorithm: Algorithm = Algorithm.AES,
    val keyRenameDialogOldName: String = ""
)