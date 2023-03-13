package bassamalim.ser.core.models

import bassamalim.ser.core.enums.Algorithm
import bassamalim.ser.core.utils.Converter

class StoreKey(
    name: String,
    val public: String
): Key(name, Algorithm.RSA) {

    fun asBytes(): ByteArray = Converter.decode(public)

    fun asPublicKey() = Converter.toPublicKey(public)

}