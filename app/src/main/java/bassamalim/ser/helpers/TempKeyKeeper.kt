package bassamalim.ser.helpers

import android.content.SharedPreferences
import bassamalim.ser.data.Prefs
import com.google.gson.Gson
import java.security.KeyPair
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

    fun storeRSAKey(key: KeyPair) {
        val keyJson = gson.toJson(key)
        sp.edit()
            .putString(Prefs.RSAKey.key, keyJson)
            .apply()
    }

    fun getRSAKey(): KeyPair? {
        val keyJson = sp.getString(
            Prefs.RSAKey.key,
            null
        ) ?: return null
        return gson.fromJson(keyJson, KeyPair::class.java)
    }

}