package bassamalim.ser.repository

import bassamalim.ser.data.database.AppDatabase
import bassamalim.ser.helpers.TempKeyKeeper
import com.google.gson.Gson
import java.security.KeyPair
import javax.inject.Inject

class RSARepo @Inject constructor(
    private val db: AppDatabase,
    private val gson: Gson,
    private val tempKeyKeeper: TempKeyKeeper
) {

    fun storeTempKey(key: KeyPair) = tempKeyKeeper.storeRSAKey(key)
    fun getTempKey() = tempKeyKeeper.getRSAKey()

    fun storeKey(name: String, keyPair: KeyPair) {
        val keyJson = gson.toJson(keyPair)
        db.rsaDao().insert(name, keyJson)
    }
    fun getKey(name: String): KeyPair {
        val keyJson = db.rsaDao().getKey(name)
        return gson.fromJson(keyJson, KeyPair::class.java)
    }

    fun getKeyNames() = db.rsaDao().getNames()

}