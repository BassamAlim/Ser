package bassamalim.ser.features.keyPicker

import bassamalim.ser.core.models.Key

data class KeyPickerState(
    val loading: Boolean = false,
    val fromKeyStore: Boolean = false,
    val items: List<Key> = emptyList()
)