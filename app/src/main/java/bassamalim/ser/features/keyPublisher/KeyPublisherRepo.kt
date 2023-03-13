package bassamalim.ser.features.keyPublisher

import android.content.SharedPreferences
import bassamalim.ser.core.data.Prefs
import bassamalim.ser.core.data.database.AppDatabase
import bassamalim.ser.core.helpers.KeyKeeper
import bassamalim.ser.core.models.StoreKey
import bassamalim.ser.core.utils.PrefUtils
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class KeyPublisherRepo @Inject constructor(
    private val sp: SharedPreferences,
    private val db: AppDatabase,
    private val firestore: FirebaseFirestore,
    private val keyKeeper: KeyKeeper
) {

    fun getKeyNames() = db.rsaDao().getNames()

    fun getKey(name: String) = keyKeeper.getRSAKey(name)

    suspend fun nameExists(name: String): Boolean {
        var exists = true

        firestore.collection("PublicKeys")
            .whereEqualTo("name", name)
            .get()
            .addOnSuccessListener { result ->
                exists = !result.isEmpty
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
            .await()

        return exists
    }

    suspend fun publishKey(key: StoreKey) {
        firestore.collection("PublicKeys")
            .document(key.deviceId)
            .set(key)
            .addOnSuccessListener {
                println("DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                println("Error writing document: $e")
            }
            .await()
    }

    fun getUserName() = PrefUtils.getString(sp, Prefs.UserName)
    fun setUserName(name: String) {
        sp.edit()
            .putString(Prefs.UserName.key, name)
            .apply()
    }

    fun setPublishedKeyName(name: String) {
        sp.edit()
            .putString(Prefs.PublishedKeyName.key, name)
            .apply()
    }

}