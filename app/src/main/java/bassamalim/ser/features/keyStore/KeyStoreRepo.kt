package bassamalim.ser.features.keyStore

import android.content.SharedPreferences
import bassamalim.ser.core.data.Prefs
import bassamalim.ser.core.models.StoreKey
import bassamalim.ser.core.utils.PrefUtils
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class KeyStoreRepo @Inject constructor(
    private val sp: SharedPreferences,
    private val firestore: FirebaseFirestore
) {

    suspend fun deviceRegistered(deviceId: String): Boolean {
        var registered = true

        firestore.collection("PublicKeys")
            .whereEqualTo("deviceId", deviceId)
            .get()
            .addOnSuccessListener { result ->
                registered = !result.isEmpty
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
            .await()

        return registered
    }

    suspend fun removeKey(deviceId: String) {
        firestore.collection("PublicKeys")
            .document(deviceId)
            .delete()
            .addOnSuccessListener {
                println("DocumentSnapshot successfully deleted!")
            }
            .addOnFailureListener { e ->
                println("Error deleting document: $e")
            }
            .await()
    }

    suspend fun getKeys(): List<StoreKey> {
        val items = mutableListOf<StoreKey>()

        firestore.collection("PublicKeys")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    println(document.data["name"])
                    items.add(
                        StoreKey(
                            name = document.data["name"].toString(),
                            value = document.data["value"].toString(),
                            deviceId = document.data["deviceId"].toString(),
                            published = document.data["published"] as Timestamp
                        )
                    )
                }
                println("Data retrieved successfully!")
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
            .await()

        return items
    }

    fun getUserName() = PrefUtils.getString(sp, Prefs.UserName)
    fun setUserName(name: String) {
        sp.edit()
            .putString(Prefs.UserName.key, name)
            .apply()
    }

    fun getPublishedKeyName() = PrefUtils.getString(sp, Prefs.PublishedKeyName)
    fun setPublishedKeyName(name: String) {
        sp.edit()
            .putString(Prefs.PublishedKeyName.key, name)
            .apply()
    }

}