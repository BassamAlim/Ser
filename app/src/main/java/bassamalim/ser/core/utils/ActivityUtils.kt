package bassamalim.ser.core.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import bassamalim.ser.R
import bassamalim.ser.core.enums.Language
import bassamalim.ser.core.enums.Theme
import java.util.*

object ActivityUtils {

    fun onActivityCreateSetTheme(context: Context): Theme {
        val theme = PrefUtils.getTheme(PrefUtils.getPreferences(context))
        when (theme) {
            Theme.DARK -> context.setTheme(R.style.Theme_Dark)
            else -> context.setTheme(R.style.Theme_Dark)
        }
        return theme
    }

    fun onActivityCreateSetLocale(context: Context): Language {
        val language = PrefUtils.getLanguage(PrefUtils.getPreferences(context))

        val locale = Locale(
            if (language == Language.ENGLISH) "en"
            else "ar"
        )
        Locale.setDefault(locale)
        val resources = context.resources

        val configuration = resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        resources.updateConfiguration(configuration, resources.displayMetrics)

        return language
    }

    fun restartActivity(activity: Activity) {
        activity.finish()
        activity.startActivity(activity.intent)
    }

    fun clearAppData(ctx: Context) {
        try {
            ctx.getSystemService(ActivityManager::class.java).clearApplicationUserData()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}