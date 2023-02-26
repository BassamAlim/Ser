package bassamalim.ser.repository

import android.content.SharedPreferences
import android.content.res.Resources
import bassamalim.ser.utils.PrefUtils
import javax.inject.Inject

class MainRepo @Inject constructor(
    private val res: Resources,
    val sp: SharedPreferences
) {

    val numeralsLanguage = PrefUtils.getNumeralsLanguage(sp)

}