package bassamalim.ser.features.aes

import android.content.SharedPreferences
import bassamalim.ser.core.data.Prefs
import bassamalim.ser.core.data.database.AppDatabase
import bassamalim.ser.core.helpers.KeyKeeper
import bassamalim.ser.core.models.AESKey
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