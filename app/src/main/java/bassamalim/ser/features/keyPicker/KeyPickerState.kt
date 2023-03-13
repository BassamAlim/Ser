package bassamalim.ser.features.keyPicker

import bassamalim.ser.core.models.Key

data class KeyPickerState(
    val items: List<Key> = emptyList()
)