package bassamalim.ser.core.data.database.dbs

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rsa_keys")
class RSADB(
    @field:ColumnInfo(name = "name") @field:PrimaryKey val name: String,
    @field:ColumnInfo(name = "key") val key: String
)