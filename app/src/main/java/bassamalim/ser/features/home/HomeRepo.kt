package bassamalim.ser.features.home

import android.content.SharedPreferences
import android.content.res.Resources
import bassamalim.ser.core.data.Prefs
import bassamalim.ser.core.enums.Language
import bassamalim.ser.core.utils.PrefUtils
import javax.inject.Inject

class HomeRepo @Inject constructor(
    private val res: Resources,
    val sp: SharedPreferences
) {

}