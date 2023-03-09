package bassamalim.ser.repository

import bassamalim.ser.data.database.AppDatabase
import bassamalim.ser.helpers.KeyKeeper
import bassamalim.ser.models.AESKey
import bassamalim.ser.models.MyKeyPair
import bassamalim.ser.models.RSAKeyPair
import bassamalim.ser.utils.Converter
import javax.inject.Inject

class KeysRepo @Inject constructor(
    private val db: AppDatabase,
    private val keyKeeper: KeyKeeper
) {

    fun getAESKeys(): List<AESKey> = keyKeeper.getAllAES()

    fun getRSAKeys(): List<RSAKeyPair> = keyKeeper.getAllRSA()

    fun insertAESKey(name: String, key: String) {
        val keyObj = AESKey(
            name = name,
            secret = Converter.toSecretKey(key)
        )
        keyKeeper.storeAESKey(keyObj)
    }

    fun insertRSAKey(name: String, public: String, private: String) {
        val keyObj = RSAKeyPair(
            name,
            MyKeyPair(
                Converter.toPublicKey(public),
                Converter.toPrivateKey(private)
            )
        )
        keyKeeper.storeRSAKey(keyObj)
    }

    fun removeAESKey(name: String) = db.aesDao().delete(name)

    fun removeRSAKey(name: String) = db.rsaDao().delete(name)

    fun getAESKeyNames() = db.aesDao().getNames()

    fun getRSAKeyNames() = db.rsaDao().getNames()

}