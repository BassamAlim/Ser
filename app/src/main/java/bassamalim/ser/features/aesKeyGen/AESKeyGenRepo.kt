package bassamalim.ser.features.aesKeyGen

import android.content.SharedPreferences
import bassamalim.ser.core.data.Prefs
import bassamalim.ser.core.data.database.AppDatabase
import bassamalim.ser.core.helpers.KeyKeeper
import bassamalim.ser.core.models.AESKey
import javax.inject.Inject

class AESKeyGenRepo @Inject constructor(
    private val sp: SharedPreferences,
    private val db: AppDatabase,
    private val keyKeeper: KeyKeeper
) {

    fun getKeyNames() = db.aesDao().getNames()

    fun storeKey(key: AESKey) = keyKeeper.storeAESKey(key)

    fun setSelectedKey(name: String) {
        sp.edit()
            .putString(Prefs.SelectedAESKeyName.key, name)
            .apply()
    }

}