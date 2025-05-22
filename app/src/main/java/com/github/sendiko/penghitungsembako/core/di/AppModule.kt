package com.github.sendiko.penghitungsembako.core.di

import com.github.sendiko.penghitungsembako.core.database.AppDatabase
import com.github.sendiko.penghitungsembako.core.preferences.UserPreferences
import com.github.sendiko.penghitungsembako.sembako.core.data.SembakoDao

interface AppModule {

    val database: AppDatabase

    val sembakoDao: SembakoDao

    val userPreferences: UserPreferences

}