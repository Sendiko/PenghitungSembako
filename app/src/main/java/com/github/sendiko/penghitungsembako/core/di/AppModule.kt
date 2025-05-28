package com.github.sendiko.penghitungsembako.core.di

import android.app.Application
import com.github.sendiko.penghitungsembako.core.database.AppDatabase
import com.github.sendiko.penghitungsembako.core.network.ApiService
import com.github.sendiko.penghitungsembako.core.preferences.UserPreferences
import com.github.sendiko.penghitungsembako.grocery.core.data.SembakoDao
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface AppModule {

    val database: AppDatabase

    val sembakoDao: SembakoDao

    val userPreferences: UserPreferences

    val okHttpClient: OkHttpClient

    val retrofit: Retrofit

    val apiService: ApiService

    val application: Application

}