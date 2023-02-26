package bassamalim.ser.state

import bassamalim.ser.models.AESKey
import bassamalim.ser.models.RSAKey

data class KeysState(
    val aesKeys: List<AESKey> = emptyList(),
    val rsaKeys: List<RSAKey> = emptyList()
)