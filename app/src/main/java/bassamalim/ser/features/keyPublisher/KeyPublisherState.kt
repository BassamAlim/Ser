package bassamalim.ser.features.keyPublisher

data class KeyPublisherState(
    val loading: Boolean = false,
    val nameExists: Boolean = false,
    val keyNames: List<String> = emptyList()
)