package bassamalim.ser.models

import java.security.PrivateKey
import java.security.PublicKey

data class MyKeyPair(
    val public: PublicKey,
    val private: PrivateKey
)