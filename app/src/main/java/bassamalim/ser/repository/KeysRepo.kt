package bassamalim.ser.repository

import bassamalim.ser.data.database.AppDatabase
import bassamalim.ser.models.AESKey
import bassamalim.ser.models.RSAKey
import com.google.gson.Gson
import java.security.KeyPair
import javax.inject.Inject

class KeysRepo @Inject constructor(
    private val db: AppDatabase,
    private val gson: Gson
) {

    fun getAESKeys(): List<AESKey> {
        return db.aesDao().getAll().map {
            AESKey(it.name, it.key)
        }
    }

    fun getRSAKeys(): List<RSAKey> {
        return db.rsaDao().getAll().map {
            val key = gson.fromJson(it.key, KeyPair::class.java)
            RSAKey(it.name, key)
        }
    }

}