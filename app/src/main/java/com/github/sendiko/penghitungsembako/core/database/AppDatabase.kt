package com.github.sendiko.penghitungsembako.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.sendiko.penghitungsembako.grocery.core.data.GroceryEntity
import com.github.sendiko.penghitungsembako.grocery.core.data.GroceryDao

@Database(
    entities = [GroceryEntity::class],
    version = 4
)
abstract class AppDatabase : RoomDatabase() {

    abstract val sembakoDao: GroceryDao

}