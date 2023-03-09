package bassamalim.ser.repository

import android.content.SharedPreferences
import android.content.res.Resources
import bassamalim.ser.data.Prefs
import bassamalim.ser.enums.Language
import bassamalim.ser.utils.PrefUtils
import javax.inject.Inject

class HomeRepo @Inject constructor(
    private val res: Resources,
    val sp: SharedPreferences
) {

}