package bassamalim.ser.repository

import android.content.SharedPreferences
import android.content.res.Resources
import bassamalim.ser.R
import javax.inject.Inject

class SettingsRepo @Inject constructor(
    private val res: Resources,
    val sp: SharedPreferences
) {

    fun getSelectStr() = res.getString(R.string.select)
    fun getCancelStr() = res.getString(R.string.cancel)

}