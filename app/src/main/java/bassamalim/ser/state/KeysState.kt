package bassamalim.ser.state

import bassamalim.ser.models.AESKey
import bassamalim.ser.models.RSAKeyPair

data class KeysState(
    val aesKeys: List<AESKey> = emptyList(),
    val rsaKeys: List<RSAKeyPair> = emptyList(),
    val aesKeyAddDialogShown: Boolean = false,
    val rsaKeyAddDialogShown: Boolean = false,
    val aesNameAlreadyExists: Boolean = false,
    val rsaNameAlreadyExists: Boolean = false,
    val invalidAESKey: Boolean = false,
    val invalidRSAKey: Boolean = false
)