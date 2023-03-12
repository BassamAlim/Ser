package bassamalim.ser.features.keys

import android.content.SharedPreferences
import bassamalim.ser.core.data.Prefs
import bassamalim.ser.core.data.database.AppDatabase
import bassamalim.ser.core.helpers.KeyKeeper
import bassamalim.ser.core.models.AESKey
import bassamalim.ser.core.models.MyKeyPair
import bassamalim.ser.core.models.RSAKeyPair
import bassamalim.ser.core.utils.Converter
import bassamalim.ser.core.utils.PrefUtils
import javax.inject.Inject

class KeysRepo @Inject constructor(
    private val sp: SharedPreferences,
    private val db: AppDatabase,
    private val keyKeeper: KeyKeeper
) {

    fun getAESKeys(): List<AESKey> = keyKeeper.getAllAES()

    fun getRSAKeys(): List<RSAKeyPair> = keyKeeper.getAllRSA()

    fun getPrimaryAESKeyName() = PrefUtils.getString(sp, Prefs.PrimaryAESKeyName)
    fun setPrimaryAESKeyName(name: String) {
        sp.edit()
            .putString(Prefs.PrimaryAESKeyName.key, name)
            .apply()
    }

    fun getPrimaryRSAKeyName() = PrefUtils.getString(sp, Prefs.PrimaryRSAKeyName)
    fun setPrimaryRSAKeyName(name: String) {
        sp.edit()
            .putString(Prefs.PrimaryRSAKeyName.key, name)
            .apply()
    }

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