package bassamalim.ser.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import bassamalim.ser.data.database.daos.*
import bassamalim.ser.data.database.dbs.*

@Database(
    entities = [AESDB::class, RSADB::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun aesDao(): AESDao
    abstract fun rsaDao(): RSADao
}
