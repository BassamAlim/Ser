package bassamalim.ser.features.welcome

import android.content.SharedPreferences
import bassamalim.ser.core.data.Prefs
import javax.inject.Inject

class WelcomeRepo @Inject constructor(
    val pref: SharedPreferences
) {

    fun unsetFirstTime() {
        pref.edit()
            .putBoolean(Prefs.FirstTime.key, false)
            .apply()
    }

}