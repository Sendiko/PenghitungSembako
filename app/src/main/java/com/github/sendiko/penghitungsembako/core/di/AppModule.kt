package com.github.sendiko.penghitungsembako.core.di

import com.github.sendiko.penghitungsembako.core.database.AppDatabase
import com.github.sendiko.penghitungsembako.sembako.core.data.SembakoDao
import com.github.sendiko.penghitungsembako.sembako.dashboard.presentation.DashboardViewModel

interface AppModule {

    val database: AppDatabase

    val sembakoDao: SembakoDao

}