package bassamalim.ser.helpers

import android.content.SharedPreferences
import bassamalim.ser.data.Prefs
import bassamalim.ser.models.MyByteKeyPair
import bassamalim.ser.models.MyKeyPair
import bassamalim.ser.utils.Utils
import com.google.gson.Gson
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class TempKeyKeeper(
    private val sp: SharedPreferences,
    private val gson: Gson
) {

    fun storeAESKey(key: SecretKey) {
        val encodedKey = key.encoded
        val keyJson = gson.toJson(encodedKey)
        sp.edit()
            .putString(Prefs.AESKey.key, keyJson)
            .apply()
    }

    fun getAESKey(): SecretKey? {
        val keyJson = sp.getString(
            Prefs.AESKey.key,
            null
        ) ?: return null
        val encodedKey = gson.fromJson(keyJson, ByteArray::class.java)
        return SecretKeySpec(encodedKey, "AES")
    }

    fun storeRSAKey(key: MyKeyPair) {
        val byteKeyPair = Utils.toStore(key)
        val keyJson = gson.toJson(byteKeyPair)
        sp.edit()
            .putString(Prefs.RSAKey.key, keyJson)
            .apply()
    }

    fun getRSAKey(): MyKeyPair? {
        val keyPairJson = sp.getString(
            Prefs.RSAKey.key,
            null
        ) ?: return null
        val byteKeyPair = gson.fromJson(keyPairJson, MyByteKeyPair::class.java)
        return Utils.fromStore(byteKeyPair)
    }

}