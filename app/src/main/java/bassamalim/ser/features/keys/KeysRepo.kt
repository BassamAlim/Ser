package bassamalim.ser.features.keys

import android.content.SharedPreferences
import bassamalim.ser.core.data.Prefs
import bassamalim.ser.core.data.database.AppDatabase
import bassamalim.ser.core.helpers.KeyKeeper
import bassamalim.ser.core.models.AESKey
import bassamalim.ser.core.models.RSAKeyPair
import bassamalim.ser.core.utils.PrefUtils
import javax.inject.Inject

class KeysRepo @Inject constructor(
    private val sp: SharedPreferences,
    private val db: AppDatabase,
    private val keyKeeper: KeyKeeper
) {

    fun getAESKeys(): List<AESKey> = keyKeeper.getAllAES()

    fun getRSAKeys(): List<RSAKeyPair> = keyKeeper.getAllRSA()

    fun getPublishedKeyName() = PrefUtils.getString(sp, Prefs.PublishedKeyName)

    fun removeAESKey(name: String) = db.aesDao().delete(name)

    fun removeRSAKey(name: String) = db.rsaDao().delete(name)

    fun getAESKeyNames() = db.aesDao().getNames()

    fun getRSAKeyNames() = db.rsaDao().getNames()

}