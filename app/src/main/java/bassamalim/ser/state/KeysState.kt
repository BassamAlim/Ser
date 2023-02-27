package bassamalim.ser.state

import bassamalim.ser.models.AESKey
import bassamalim.ser.models.RSAKey

data class KeysState(
    val aesKeys: List<AESKey> = emptyList(),
    val rsaKeys: List<RSAKey> = emptyList(),
    val aesKeyAddDialogShown: Boolean = false,
    val rsaKeyAddDialogShown: Boolean = false,
    val aesNameAlreadyExists: Boolean = false,
    val rsaNameAlreadyExists: Boolean = false,
    val invalidAESKey: Boolean = false,
    val invalidRSAKey: Boolean = false
)