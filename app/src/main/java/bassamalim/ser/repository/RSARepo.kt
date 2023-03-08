package bassamalim.ser.repository

import android.content.SharedPreferences
import bassamalim.ser.data.Prefs
import bassamalim.ser.data.database.AppDatabase
import bassamalim.ser.enums.Algorithm
import bassamalim.ser.helpers.KeyKeeper
import bassamalim.ser.models.MyKeyPair
import com.google.gson.Gson
import javax.inject.Inject

class RSARepo @Inject constructor(
    private val sp: SharedPreferences,
    private val db: AppDatabase,
    gson: Gson
) {

    private val keyKeeper = KeyKeeper(db, gson, Algorithm.RSA)

    fun storeKey(name: String, keyPair: MyKeyPair) {
        keyKeeper.store(name, keyPair)
    }
    fun getKey(name: String): MyKeyPair {
        return keyKeeper.get(name) as MyKeyPair
    }

    fun setSelectedKey(name: String) {
        sp.edit()
            .putString(Prefs.SelectedRSAKey.key, name)
            .apply()
    }

    fun getKeyNames() = db.rsaDao().getNames()

}