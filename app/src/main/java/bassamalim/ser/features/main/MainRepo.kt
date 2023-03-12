package bassamalim.ser.features.main

import android.content.SharedPreferences
import android.content.res.Resources
import bassamalim.ser.core.utils.PrefUtils
import javax.inject.Inject

class MainRepo @Inject constructor(
    private val res: Resources,
    val sp: SharedPreferences
)