package bassamalim.ser.repository

import bassamalim.ser.data.database.AppDatabase
import bassamalim.ser.helpers.TempKeyKeeper
import com.google.gson.Gson
import javax.crypto.SecretKey
import javax.inject.Inject

class AESRepo @Inject constructor(
    private val db: AppDatabase,
    private val gson: Gson,
    private val tempKeyKeeper: TempKeyKeeper
) {

    fun storeTempKey(key: SecretKey) = tempKeyKeeper.storeAESKey(key)
    fun getTempKey() = tempKeyKeeper.getAESKey()

    fun storeKey(name: String, key: SecretKey) {
        val keyJson = gson.toJson(key)
        db.aesDao().insert(name, keyJson)
    }
    fun getKey(name: String): SecretKey {
        val keyJson = db.aesDao().getKey(name)
        return gson.fromJson(keyJson, SecretKey::class.java)
    }

    fun getKeyNames() = db.aesDao().getNames()

}