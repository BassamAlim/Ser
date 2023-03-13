package bassamalim.ser.features.keyPicker

import android.content.SharedPreferences
import bassamalim.ser.core.data.Prefs
import bassamalim.ser.core.helpers.KeyKeeper
import javax.inject.Inject

class KeyPickerRepo @Inject constructor(
    private val sp: SharedPreferences,
    private val keyKeeper: KeyKeeper
) {

    fun getAESKeys() = keyKeeper.getAllAES()

    fun getRSAKeys() = keyKeeper.getAllRSA()

    fun setAESSelectedKey(name: String) {
        sp.edit()
            .putString(Prefs.SelectedAESKeyName.key, name)
            .apply()
    }

    fun setRSSelectedKey(name: String) {
        sp.edit()
            .putString(Prefs.SelectedRSAKeyName.key, name)
            .apply()
    }

}