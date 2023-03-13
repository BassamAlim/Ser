package bassamalim.ser.features.rsa

import bassamalim.ser.core.helpers.KeyKeeper
import javax.inject.Inject

class RSARepo @Inject constructor(
    private val keyKeeper: KeyKeeper
) {

    fun getKey(name: String) = keyKeeper.getRSAKey(name)

}