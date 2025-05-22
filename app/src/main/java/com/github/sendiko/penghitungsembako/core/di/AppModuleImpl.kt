package com.github.sendiko.penghitungsembako.core.di

import android.content.Context
import androidx.room.Room
import com.github.sendiko.penghitungsembako.core.database.AppDatabase
import com.github.sendiko.penghitungsembako.core.preferences.UserPreferences
import com.github.sendiko.penghitungsembako.core.preferences.dataStore
import com.github.sendiko.penghitungsembako.sembako.core.data.SembakoDao

class AppModuleImpl(
    val context: Context
): AppModule {
    override val database: AppDatabase
        get() = Room
            .databaseBuilder(context, AppDatabase::class.java, "sembako.db")
            .build()
    override val sembakoDao: SembakoDao
        get() = database.sembakoDao
    override val userPreferences: UserPreferences
        get() = UserPreferences(context.dataStore)
}