package bassamalim.ser.features.rsaKeyGen

import android.content.SharedPreferences
import bassamalim.ser.core.data.Prefs
import bassamalim.ser.core.data.database.AppDatabase
import bassamalim.ser.core.helpers.KeyKeeper
import bassamalim.ser.core.models.RSAKeyPair
import javax.inject.Inject

class RSAKeyGenRepo @Inject constructor(
    private val sp: SharedPreferences,
    private val db: AppDatabase,
    private val keyKeeper: KeyKeeper
) {

    fun getKeyNames() = db.rsaDao().getNames()

    fun storeKey(key: RSAKeyPair) = keyKeeper.storeRSAKey(key)

    fun setSelectedKey(name: String) {
        sp.edit()
            .putString(Prefs.SelectedRSAKeyName.key, name)
            .apply()
    }

}