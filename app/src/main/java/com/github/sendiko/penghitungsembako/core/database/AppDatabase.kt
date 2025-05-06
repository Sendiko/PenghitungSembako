package com.github.sendiko.penghitungsembako.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.sendiko.penghitungsembako.sembako.core.data.Sembako
import com.github.sendiko.penghitungsembako.sembako.core.data.SembakoDao

@Database(
    entities = [Sembako::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract val sembakoDao: SembakoDao

}