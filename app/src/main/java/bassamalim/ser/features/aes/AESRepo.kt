package bassamalim.ser.features.aes

import bassamalim.ser.core.helpers.KeyKeeper
import javax.inject.Inject

class AESRepo @Inject constructor(
    private val keyKeeper: KeyKeeper
) {

    fun getKey(name: String) = keyKeeper.getAESKey(name)

}