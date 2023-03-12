package bassamalim.ser.features.keyStore

import bassamalim.ser.core.models.StoreKey
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class KeyStoreRepo @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun getKeys(): List<StoreKey> {
        val items = mutableListOf<StoreKey>()

        firestore.collection("PublicKeys")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    items.add(
                        StoreKey(
                            name = document.data["name"].toString(),
                            value = document.data["value"].toString(),
                            deviceId = document.data["deviceId"].toString(),
                            created = document.data["created"].toString()
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

}