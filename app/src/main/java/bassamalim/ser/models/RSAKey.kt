package bassamalim.ser.models

import java.security.KeyPair

data class RSAKey(
    val name: String,
    val keyPair: KeyPair,
)
