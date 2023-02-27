package bassamalim.ser.repository

import bassamalim.ser.data.database.AppDatabase
import bassamalim.ser.helpers.TempKeyKeeper
import bassamalim.ser.models.MyByteKeyPair
import bassamalim.ser.models.MyKeyPair
import bassamalim.ser.utils.Utils
import com.google.gson.Gson
import javax.inject.Inject

class RSARepo @Inject constructor(
    private val db: AppDatabase,
    private val gson: Gson,
    private val tempKeyKeeper: TempKeyKeeper
) {

    fun storeTempKey(key: MyKeyPair) = tempKeyKeeper.storeRSAKey(key)
    fun getTempKey() = tempKeyKeeper.getRSAKey()

    fun storeKey(name: String, keyPair: MyKeyPair) {
        val byteKeyPair = Utils.toStore(keyPair)
        val keyJson = gson.toJson(byteKeyPair)
        db.rsaDao().insert(name, keyJson)
    }
    fun getKey(name: String): MyKeyPair {
        val keyJson = db.rsaDao().getKey(name)
        val byteKeyPair = gson.fromJson(keyJson, MyByteKeyPair::class.java)
        return Utils.fromStore(byteKeyPair)
    }

    fun getKeyNames() = db.rsaDao().getNames()

}