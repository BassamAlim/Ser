package bassamalim.ser.helpers

import bassamalim.ser.data.database.AppDatabase
import bassamalim.ser.models.AESKey
import bassamalim.ser.models.MyByteKeyPair
import bassamalim.ser.models.RSAKeyPair
import bassamalim.ser.utils.Converter
import com.google.gson.Gson
import javax.inject.Inject

class KeyKeeper @Inject constructor(
    private val db: AppDatabase,
    private val gson: Gson
) {

    fun storeAESKey(key: AESKey) {
        val bytes = key.asBytes()
        val json = gson.toJson(bytes)
        db.aesDao().insert(key.name, json)
    }

    fun getAESKey(name: String): AESKey {
        val json = db.aesDao().getKey(name)
        val bytes = gson.fromJson(json, ByteArray::class.java)
        return AESKey(
            name,
            Converter.toSecretKey(bytes)
        )
    }

    fun getAllAES(): List<AESKey> {
        val keys = db.aesDao().getAll()
        return keys.map {
            val bytes = gson.fromJson(it.key, ByteArray::class.java)
            AESKey(
                it.name,
                Converter.toSecretKey(bytes)
            )
        }
    }


    fun storeRSAKey(key: RSAKeyPair) {
        val byteKeyPair = Converter.convert(key.key)
        val json = gson.toJson(byteKeyPair)
        db.rsaDao().insert(key.name, json)
    }

    fun getRSAKey(name: String): RSAKeyPair {
        val json = db.rsaDao().getKey(name)
        val keyPairBytes = gson.fromJson(json, MyByteKeyPair::class.java)
        return RSAKeyPair(
            name,
            Converter.convert(keyPairBytes)
        )
    }

    fun getAllRSA(): List<RSAKeyPair> {
        val keys = db.rsaDao().getAll()
        return keys.map {
            val keyPairBytes = gson.fromJson(it.key, MyByteKeyPair::class.java)
            RSAKeyPair(
                it.name,
                Converter.convert(keyPairBytes)
            )
        }
    }

}