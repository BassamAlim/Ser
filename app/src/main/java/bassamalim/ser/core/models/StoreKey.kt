package bassamalim.ser.core.models

import com.google.firebase.Timestamp

data class StoreKey(
    val name: String,
    val value: String,
    val deviceId: String,
    val created: Timestamp
)