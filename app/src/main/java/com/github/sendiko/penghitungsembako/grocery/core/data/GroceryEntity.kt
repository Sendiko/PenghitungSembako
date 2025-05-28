package com.github.sendiko.penghitungsembako.grocery.core.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sembako")
data class GroceryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val unit: String,
    val pricePerUnit: Double,
    val imageUrl: String,
)