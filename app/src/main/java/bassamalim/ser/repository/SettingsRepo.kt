package bassamalim.ser.repository

import android.content.SharedPreferences
import android.content.res.Resources
import bassamalim.ser.R
import bassamalim.ser.utils.PrefUtils
import javax.inject.Inject

class SettingsRepo @Inject constructor(
    private val resources: Resources,
    val sp: SharedPreferences
) {

    val numeralsLanguage = PrefUtils.getNumeralsLanguage(sp)

    fun getSelectStr() = resources.getString(R.string.select)
    fun getCancelStr() = resources.getString(R.string.cancel)

}