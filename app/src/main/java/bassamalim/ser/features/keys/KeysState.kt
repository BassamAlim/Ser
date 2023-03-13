package bassamalim.ser.features.keys

import bassamalim.ser.core.models.AESKey
import bassamalim.ser.core.models.RSAKeyPair

data class KeysState(
    val aesKeys: List<AESKey> = emptyList(),
    val rsaKeys: List<RSAKeyPair> = emptyList(),
    val publishedKeyValue: String = "",
    val aesKeyAddDialogShown: Boolean = false,
    val rsaKeyAddDialogShown: Boolean = false,
    val aesNameAlreadyExists: Boolean = false,
    val rsaNameAlreadyExists: Boolean = false,
    val invalidAESKey: Boolean = false,
    val invalidRSAKey: Boolean = false
)