package bassamalim.ser.core.models

import bassamalim.ser.core.enums.Algorithm

class RSAKeyPair(
    name: String,
    val key: MyKeyPair,
): Key(name, Algorithm.RSA)