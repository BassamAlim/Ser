package bassamalim.ser.core.models

import bassamalim.ser.core.enums.Algorithm
import bassamalim.ser.core.utils.Converter
import javax.crypto.SecretKey

class AESKey(
    name: String,
    val secret: SecretKey
): Key(name, Algorithm.AES) {

    fun asBytes(): ByteArray = secret.encoded

    fun asString() = Converter.encode(asBytes())

}