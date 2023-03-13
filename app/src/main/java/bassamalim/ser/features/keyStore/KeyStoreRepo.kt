package bassamalim.ser.features.keyStore

import android.content.SharedPreferences
import bassamalim.ser.core.data.Prefs
import bassamalim.ser.core.helpers.KeyKeeper
import bassamalim.ser.core.models.RSAKeyPair
import bassamalim.ser.core.models.StoreKey
import bassamalim.ser.core.utils.PrefUtils
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class KeyStoreRepo @Inject constructor(
    private val sp: SharedPreferences,
    private val firestore: FirebaseFirestore,
    private val keyKeeper: KeyKeeper
) {

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
                            created = document.data["created"] as Timestamp
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

    fun getPublishedKey(): RSAKeyPair {
        val publishedKeyValue = PrefUtils.getString(sp, Prefs.PublishedKeyValue)
        return keyKeeper.getRSAKey(publishedKeyValue)
    }

}