package bassamalim.ser.helpers

import bassamalim.ser.data.database.AppDatabase
import bassamalim.ser.enums.Algorithm
import bassamalim.ser.models.MyByteKeyPair
import bassamalim.ser.models.MyKeyPair
import bassamalim.ser.utils.Utils
import com.google.gson.Gson
import javax.crypto.SecretKey

class KeyKeeper(
    private val db: AppDatabase,
    private val gson: Gson,
    private val algo: Algorithm
) {

    fun store(name: String, key: Any) {
        when (algo) {
            Algorithm.AES -> storeAESKey(name, key as SecretKey)
            Algorithm.RSA -> storeRSAKey(name, key as MyKeyPair)
        }
    }

    fun get(name: String): Any {
        return when (algo) {
            Algorithm.AES -> getAESKey(name)
            Algorithm.RSA -> getRSAKey(name)
        }
    }

    private fun storeAESKey(name: String, key: SecretKey) {
        val encoded = Utils.toStore(key)
        val keyJson = gson.toJson(encoded)
        db.aesDao().insert(name, keyJson)
    }

    private fun getAESKey(name: String): SecretKey {
        val keyJson = db.aesDao().getKey(name)
        val encoded = gson.fromJson(keyJson, ByteArray::class.java)
        return Utils.fromStore(encoded)
    }

    private fun storeRSAKey(name: String, keyPair: MyKeyPair) {
        val byteKeyPair = Utils.toStore(keyPair)
        val keyJson = gson.toJson(byteKeyPair)
        db.rsaDao().insert(name, keyJson)
    }

    private fun getRSAKey(name: String): MyKeyPair {
        val keyJson = db.rsaDao().getKey(name)
        val byteKeyPair = gson.fromJson(keyJson, MyByteKeyPair::class.java)
        return Utils.fromStore(byteKeyPair)
    }

}