package bassamalim.ser.features.keyRename

import android.content.SharedPreferences
import bassamalim.ser.core.data.Prefs
import bassamalim.ser.core.data.database.AppDatabase
import bassamalim.ser.core.utils.PrefUtils
import javax.inject.Inject

class KeyRenameRepo @Inject constructor(
    private val sp: SharedPreferences,
    private val db: AppDatabase
) {

    fun getAESKeyNames() = db.aesDao().getNames()

    fun getRSAKeyNames() = db.rsaDao().getNames()

    fun renameAESKey(oldName: String, newName: String) =
        db.aesDao().rename(oldName, newName)

    fun renameRSAKey(oldName: String, newName: String) =
        db.rsaDao().rename(oldName, newName)

    fun getPublishedKeyName() = PrefUtils.getString(sp, Prefs.PublishedKeyName)
    fun setPublishedKeyName(name: String) {
        sp.edit()
            .putString(Prefs.PublishedKeyName.key, name)
            .apply()
    }

}