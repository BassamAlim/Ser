package bassamalim.ser.core.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.room.Room
import bassamalim.ser.core.data.GlobalVals
import bassamalim.ser.core.data.Prefs
import bassamalim.ser.core.data.database.AppDatabase

object DBUtils {

    fun getDB(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "SerDB")
            .createFromAsset("databases/SerDB.db")
            .allowMainThreadQueries()
            .build()
    }

    fun testDB(
        context: Context,
        pref: SharedPreferences = PrefUtils.getPreferences(context)
    ) {
        val lastVer = PrefUtils.getInt(pref, Prefs.LastDBVersion)
        if (GlobalVals.dbVer > lastVer) reviveDB(context)

        try {  // if there is a problem in the db it will cause an error
            getDB(context).aesDao().getAll()
        } catch (e: Exception) {
            reviveDB(context)
        }
    }

    fun reviveDB(context: Context) {
        val pref = PrefUtils.getPreferences(context)

        context.deleteDatabase("SerDB")

        pref.edit()
            .putInt(Prefs.LastDBVersion.key, GlobalVals.dbVer)
            .apply()

        Log.i(GlobalVals.TAG, "Database Revived")
    }

}