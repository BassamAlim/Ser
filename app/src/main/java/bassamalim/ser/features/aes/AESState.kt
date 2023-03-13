package bassamalim.ser.features.aes

import bassamalim.ser.core.enums.Operation

data class AESState(
    val keyName: String = "",
    val secretKey: String = "",
    val operation: Operation = Operation.ENCRYPT,
    val result: String = "",
    val newKeyDialogShown: Boolean = false,
    val keyPickerShown: Boolean = false
)