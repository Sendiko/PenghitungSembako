package com.github.sendiko.penghitungsembako.core.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.github.sendiko.penghitungsembako.core.database.AppDatabase
import com.github.sendiko.penghitungsembako.core.network.ApiService
import com.github.sendiko.penghitungsembako.core.network.BASE_URL
import com.github.sendiko.penghitungsembako.core.preferences.UserPreferences
import com.github.sendiko.penghitungsembako.core.preferences.dataStore
import com.github.sendiko.penghitungsembako.sembako.core.data.SembakoDao
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.jvm.java

class AppModuleImpl(
    private val app: Application
): AppModule {
    override val database: AppDatabase
        get() = Room
            .databaseBuilder(app.applicationContext, AppDatabase::class.java, "sembako.db")
            .build()

    override val sembakoDao: SembakoDao
        get() = database.sembakoDao

    override val userPreferences: UserPreferences
        get() = UserPreferences(app.applicationContext.dataStore)

    override val okHttpClient: OkHttpClient
        get() = OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(chain.request().newBuilder().also {
                    it.addHeader("Accept", "application/json")
                }.build())
            }.also {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                it.addInterceptor(logging)
            }
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

    override val retrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    override val apiService: ApiService
        get() = retrofit.create(ApiService::class.java)
    override val application: Application
        get() = app

}