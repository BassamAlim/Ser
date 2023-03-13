package bassamalim.ser.core.models

import bassamalim.ser.core.enums.Algorithm

abstract class Key(
    val name: String,
    val type: Algorithm
)