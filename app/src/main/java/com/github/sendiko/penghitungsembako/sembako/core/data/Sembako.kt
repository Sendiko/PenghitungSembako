package com.github.sendiko.penghitungsembako.sembako.core.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sembako")
data class Sembako(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val unit: String,
    val pricePerUnit: Double,
)