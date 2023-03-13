package bassamalim.ser.features.keyPicker

import android.content.SharedPreferences
import bassamalim.ser.core.data.Prefs
import bassamalim.ser.core.helpers.KeyKeeper
import bassamalim.ser.core.models.StoreKey
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class KeyPickerRepo @Inject constructor(
    private val sp: SharedPreferences,
    private val firestore: FirebaseFirestore,
    private val keyKeeper: KeyKeeper
) {

    fun getAESKeys() = keyKeeper.getAllAES()

    fun getRSAKeys() = keyKeeper.getAllRSA()

    fun setAESSelectedKey(name: String) {
        sp.edit()
            .putString(Prefs.SelectedAESKeyName.key, name)
            .apply()
    }

    fun setRSASelectedKey(name: String) {
        sp.edit()
            .putString(Prefs.SelectedRSAKeyName.key, name)
            .apply()
    }

    suspend fun getStoreKeys(): List<StoreKey> {
        val items = mutableListOf<StoreKey>()

        firestore.collection("PublicKeys")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    println(document.data["name"])
                    items.add(
                        StoreKey(
                            name = document.data["name"].toString(),
                            public = document.data["value"].toString()
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