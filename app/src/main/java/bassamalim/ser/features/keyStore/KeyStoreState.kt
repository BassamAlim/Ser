package bassamalim.ser.features.keyStore

import bassamalim.ser.core.models.StoreKey

data class KeyStoreState(
    val loading: Boolean = true,
    val keyPublished: Boolean = false,
    val items: List<StoreKey> = emptyList(),
)