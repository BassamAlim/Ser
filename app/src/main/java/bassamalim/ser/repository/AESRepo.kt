package bassamalim.ser.repository

import android.content.SharedPreferences
import bassamalim.ser.data.Prefs
import bassamalim.ser.data.database.AppDatabase
import bassamalim.ser.helpers.KeyKeeper
import bassamalim.ser.models.AESKey
import javax.inject.Inject

class AESRepo @Inject constructor(
    private val sp: SharedPreferences,
    private val db: AppDatabase,
    private val keyKeeper: KeyKeeper
) {

    fun storeKey(key: AESKey) = keyKeeper.storeAESKey(key)

    fun getKey(name: String): AESKey = keyKeeper.getAESKey(name)

    fun setSelectedKey(name: String) {
        sp.edit()
            .putString(Prefs.SelectedAESKey.key, name)
            .apply()
    }

    fun getKeyNames() = db.aesDao().getNames()

}