package bassamalim.ser.data.database.daos

import androidx.room.Dao
import androidx.room.Query
import bassamalim.ser.data.database.dbs.AESDB

@Dao
interface AESDao {

    @Query("SELECT * FROM aes_keys")
    fun getAll(): List<AESDB>

    @Query("SELECT key FROM aes_keys WHERE name = :name")
    fun getKey(name: String): String

    @Query("SELECT name FROM aes_keys")
    fun getNames(): List<String>

    @Query("INSERT INTO aes_keys (name, key) VALUES (:name, :key)")
    fun insert(name: String, key: String)

}