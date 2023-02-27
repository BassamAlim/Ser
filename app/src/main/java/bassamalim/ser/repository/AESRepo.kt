package bassamalim.ser.repository

import bassamalim.ser.data.database.AppDatabase
import bassamalim.ser.helpers.TempKeyKeeper
import bassamalim.ser.utils.Utils
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
        val encoded = Utils.toStore(key)
        val keyJson = gson.toJson(encoded)
        db.aesDao().insert(name, keyJson)
    }
    fun getKey(name: String): SecretKey {
        val keyJson = db.aesDao().getKey(name)
        val encoded = gson.fromJson(keyJson, ByteArray::class.java)
        return Utils.fromStore(encoded)
    }

    fun getKeyNames() = db.aesDao().getNames()

}