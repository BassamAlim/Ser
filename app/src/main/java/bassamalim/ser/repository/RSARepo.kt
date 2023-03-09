package bassamalim.ser.repository

import android.content.SharedPreferences
import bassamalim.ser.data.Prefs
import bassamalim.ser.data.database.AppDatabase
import bassamalim.ser.helpers.KeyKeeper
import bassamalim.ser.models.RSAKeyPair
import javax.inject.Inject

class RSARepo @Inject constructor(
    private val sp: SharedPreferences,
    private val db: AppDatabase,
    private val keyKeeper: KeyKeeper
) {

    fun storeKey(keyPair: RSAKeyPair) = keyKeeper.storeRSAKey(keyPair)

    fun getKey(name: String): RSAKeyPair = keyKeeper.getRSAKey(name)

    fun setSelectedKey(name: String) {
        sp.edit()
            .putString(Prefs.SelectedRSAKey.key, name)
            .apply()
    }

    fun getKeyNames() = db.rsaDao().getNames()

}