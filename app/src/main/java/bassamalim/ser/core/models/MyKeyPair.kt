package bassamalim.ser.core.models

import bassamalim.ser.core.utils.Converter
import java.security.PrivateKey
import java.security.PublicKey

data class MyKeyPair(
    val public: PublicKey,
    val private: PrivateKey
) {

    fun publicAsBytes(): ByteArray = public.encoded

    fun privateAsBytes(): ByteArray = private.encoded

    fun publicAsString() = Converter.encode(publicAsBytes())

    fun privateAsString() = Converter.encode(privateAsBytes())

}