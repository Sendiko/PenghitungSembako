package com.github.sendiko.penghitungsembako.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.sendiko.penghitungsembako.sembako.core.data.Sembako

@Database(
    entities = [Sembako::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
}