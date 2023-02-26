package bassamalim.ser.utils

object DBUtils {

    /*fun getDB(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "###DB")
            .createFromAsset("databases/DB.db")
            .allowMainThreadQueries()
            .build()
    }*/

    /*fun testDB(
        context: Context,
        pref: SharedPreferences = PrefUtils.getPreferences(context)
    ) {
        val lastVer = PrefUtils.getInt(pref, Prefs.LastDBVersion)
        if (Global.dbVer > lastVer) reviveDB(context)

        try {  // if there is a problem in the db it will cause an error
            getDB(context).abcDao().getAll()
        } catch (e: Exception) {
            reviveDB(context)
        }
    }

    fun reviveDB(context: Context) {
        val pref = PrefUtils.getPreferences(context)

        context.deleteDatabase("###DB")

        // do something before the db is reset

        pref.edit()
            .putInt(Prefs.LastDBVersion.key, Global.dbVer)
            .apply()

        Log.i(Global.TAG, "Database Revived")
    }*/

}