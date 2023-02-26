package bassamalim.ser.state

import bassamalim.ser.enums.Operation

data class RSAState(
    val keyAvailable: Boolean = false,
    val publicKey: String = "",
    val privateKey: String = "",
    val keySaved: Boolean = false,
    val operation: Operation = Operation.ENCRYPT,
    val result: String = "",
    val saveDialogShown: Boolean = false,
    val nameAlreadyExists: Boolean = false,
    val keyPickerShown: Boolean = false
)