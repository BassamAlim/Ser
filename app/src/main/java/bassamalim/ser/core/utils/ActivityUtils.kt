package bassamalim.ser.core.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context

object ActivityUtils {

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