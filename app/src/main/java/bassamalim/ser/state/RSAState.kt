package bassamalim.ser.state

import bassamalim.ser.enums.Operation
import bassamalim.ser.models.RSAKeyPair

data class RSAState(
    val keyPair: RSAKeyPair = RSAKeyPair("", "", ""),
    val operation: Operation = Operation.ENCRYPT,
    val result: String = "",
    val newKeyDialogShown: Boolean = false,
    val nameAlreadyExists: Boolean = false,
    val keyPickerShown: Boolean = false
)