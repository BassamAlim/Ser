package bassamalim.ser.state

import bassamalim.ser.enums.Operation

data class AESState(
    val keyAvailable: Boolean = false,
    val key: String = "",
    val keySaved: Boolean = false,
    val operation: Operation = Operation.ENCRYPT,
    val result: String = "",
    val saveDialogShown: Boolean = false,
    val nameAlreadyExists: Boolean = false,
    val keyPickerShown: Boolean = false
)