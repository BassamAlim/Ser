package bassamalim.ser.features.keyStore

import bassamalim.ser.core.models.StoreKey

data class KeyStoreState(
    val loading: Boolean = true,
    val keyPublished: Boolean = false,
    val userName: String = "",
    val publishedKeyName: String = "",
    val items: List<StoreKey> = emptyList(),
    val keyPublisherShown: Boolean = false
)