package bassamalim.ser.repository

import bassamalim.ser.data.database.AppDatabase
import bassamalim.ser.models.AESKey
import bassamalim.ser.models.MyByteKeyPair
import bassamalim.ser.models.RSAKey
import bassamalim.ser.utils.Utils
import com.google.gson.Gson
import java.security.KeyPair
import javax.inject.Inject

class KeysRepo @Inject constructor(
    private val db: AppDatabase,
    private val gson: Gson
) {

    fun getAESKeys(): List<AESKey> {
        return db.aesDao().getAll().map {
            val key = gson.fromJson(it.key, ByteArray::class.java)
            val decoded = Utils.fromStore(key)
            AESKey(it.name, Utils.encode(decoded))
        }
    }

    fun insertAESKey(name: String, key: String) {
        val decoded = Utils.decode(key)
        val keyJson = gson.toJson(decoded)
        db.aesDao().insert(name, keyJson)
    }

    fun getRSAKeys(): List<RSAKey> {
        return db.rsaDao().getAll().map {
            val key = gson.fromJson(it.key, KeyPair::class.java)
            RSAKey(it.name, key)
        }
    }

    fun insertRSAKey(name: String, public: String, private: String) {
        val keyPair = MyByteKeyPair(
            Utils.decode(public),
            Utils.decode(private)
        )
        val keyJson = gson.toJson(keyPair)
        db.rsaDao().insert(name, keyJson)
    }

    fun getAESKeyNames() = db.aesDao().getNames()

    fun getRSAKeyNames() = db.rsaDao().getNames()

}