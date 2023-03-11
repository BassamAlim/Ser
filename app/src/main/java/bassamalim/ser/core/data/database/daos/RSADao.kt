package bassamalim.ser.core.data.database.daos

import androidx.room.Dao
import androidx.room.Query
import bassamalim.ser.core.data.database.dbs.RSADB

@Dao
interface RSADao {

    @Query("SELECT * FROM rsa_keys")
    fun getAll(): List<RSADB>

    @Query("SELECT key FROM rsa_keys WHERE name = :name")
    fun getKey(name: String): String

    @Query("SELECT name FROM rsa_keys")
    fun getNames(): List<String>

    @Query("INSERT INTO rsa_keys (name, key) VALUES (:name, :key)")
    fun insert(name: String, key: String)

    @Query("DELETE FROM rsa_keys WHERE name = :name")
    fun delete(name: String)

}