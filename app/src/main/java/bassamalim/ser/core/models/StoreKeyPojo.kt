package bassamalim.ser.core.models

import com.google.firebase.Timestamp

data class StoreKeyPojo(
    val name: String,
    val value: String,
    val deviceId: String,
    val published: Timestamp
)