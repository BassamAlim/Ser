package bassamalim.ser.models

import bassamalim.ser.utils.Converter
import javax.crypto.SecretKey

data class AESKey(
    val name: String,
    val secret: SecretKey
) {

    fun asBytes(): ByteArray = secret.encoded

    fun asString() = Converter.encode(asBytes())

}