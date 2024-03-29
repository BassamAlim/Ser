package bassamalim.ser.features.rsa

import bassamalim.ser.core.enums.Operation

data class RSAState(
    val keyName: String = "",
    val publicKey: String = "",
    val privateKey: String = "",
    val storeKey: Boolean = false,
    val operation: Operation = Operation.ENCRYPT,
    val importedFileName: String = "",
    val result: String = "",
    val newKeyDialogShown: Boolean = false,
    val keyPickerShown: Boolean = false,
    val shouldShowFileSaved: Int = 0
)