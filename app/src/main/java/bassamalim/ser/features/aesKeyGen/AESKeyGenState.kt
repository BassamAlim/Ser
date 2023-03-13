package bassamalim.ser.features.aesKeyGen

data class AESKeyGenState(
    val generateChecked: Boolean = false,
    val nameExists: Boolean = false,
    val valueInvalid: Boolean = false
)