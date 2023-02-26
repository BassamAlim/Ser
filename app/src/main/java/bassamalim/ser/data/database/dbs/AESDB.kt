package bassamalim.ser.data.database.dbs

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "aes_keys")
class AESDB(
    @field:ColumnInfo(name = "name") @field:PrimaryKey val name: String,
    @field:ColumnInfo(name = "key") val key: String
)