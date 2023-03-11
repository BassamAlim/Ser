package bassamalim.ser.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import bassamalim.ser.core.data.database.daos.AESDao
import bassamalim.ser.core.data.database.daos.RSADao
import bassamalim.ser.core.data.database.dbs.AESDB
import bassamalim.ser.core.data.database.dbs.RSADB

@Database(
    entities = [AESDB::class, RSADB::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun aesDao(): AESDao
    abstract fun rsaDao(): RSADao
}
