package id.my.sendiko.sembako.grocery.core.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grocery")
data class GroceryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val unit: String,
    val pricePerUnit: Double,
    val imageUrl: String,
    val remoteId: String,
)