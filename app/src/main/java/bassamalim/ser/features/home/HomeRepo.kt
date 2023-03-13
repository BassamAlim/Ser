package bassamalim.ser.features.home

import android.content.SharedPreferences
import android.content.res.Resources
import javax.inject.Inject

class HomeRepo @Inject constructor(
    private val res: Resources,
    val sp: SharedPreferences
)