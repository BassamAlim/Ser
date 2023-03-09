package bassamalim.ser.state

import bassamalim.ser.enums.Operation

data class RSAState(
    val keyName: String = "",
    val publicKey: String = "",
    val privateKey: String = "",
    val operation: Operation = Operation.ENCRYPT,
    val result: String = "",
    val newKeyDialogShown: Boolean = false,
    val nameAlreadyExists: Boolean = false,
    val keyPickerShown: Boolean = false
)