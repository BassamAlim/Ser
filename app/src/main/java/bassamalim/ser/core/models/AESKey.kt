package bassamalim.ser.core.models

import bassamalim.ser.core.utils.Converter
import javax.crypto.SecretKey

data class AESKey(
    val name: String,
    val secret: SecretKey
) {

    fun asBytes(): ByteArray = secret.encoded

    fun asString() = Converter.encode(asBytes())

}