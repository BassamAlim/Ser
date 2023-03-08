package bassamalim.ser.repository

import android.content.SharedPreferences
import bassamalim.ser.data.Prefs
import bassamalim.ser.data.database.AppDatabase
import bassamalim.ser.enums.Algorithm
import bassamalim.ser.helpers.KeyKeeper
import com.google.gson.Gson
import javax.crypto.SecretKey
import javax.inject.Inject

class AESRepo @Inject constructor(
    private val sp: SharedPreferences,
    private val db: AppDatabase,
    gson: Gson
) {

    private val keyKeeper = KeyKeeper(db, gson, Algorithm.AES)

    fun storeKey(name: String, key: SecretKey) {
        keyKeeper.store(name, key)
    }
    fun getKey(name: String): SecretKey {
        return keyKeeper.get(name) as SecretKey
    }

    fun setSelectedKey(name: String) {
        sp.edit()
            .putString(Prefs.SelectedAESKey.key, name)
            .apply()
    }

    fun getKeyNames() = db.aesDao().getNames()

}