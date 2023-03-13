package bassamalim.ser.features.rsaKeyGen

data class RSAKeyGenState(
    val generateChecked: Boolean = false,
    val nameExists: Boolean = false,
    val valueInvalid: Boolean = false
)