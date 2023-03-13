package bassamalim.ser.core.data.database.daos

import androidx.room.Dao
import androidx.room.Query
import bassamalim.ser.core.data.database.dbs.AESDB

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

    @Query("DELETE FROM aes_keys WHERE name = :name")
    fun delete(name: String)

    @Query("UPDATE aes_keys SET name = :newName WHERE name = :oldName")
    fun rename(oldName: String, newName: String)

}