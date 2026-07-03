package id.my.sendiko.sembako.core.di

import android.app.Application
import id.my.sendiko.sembako.core.database.AppDatabase
import id.my.sendiko.sembako.core.preferences.StatisticsPreferences
import id.my.sendiko.sembako.core.preferences.UserPreferences
import id.my.sendiko.sembako.grocery.core.data.GroceryDao
import id.my.sendiko.sembako.grocery.core.data.GroceryRemoteDataSource
import id.my.sendiko.sembako.history.data.HistoryDao
import id.my.sendiko.sembako.history.data.datasource.HistoryRemoteDataSource
import id.my.sendiko.sembako.statistics.data.datasource.StatisticsRemoteDataSource
import id.my.sendiko.sembako.store.core.data.datasource.StoreDataSource
import id.my.sendiko.sembako.user.core.data.UserRemoteDataSource
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface AppModule {

    val database: AppDatabase

    val sembakoDao: GroceryDao

    val historyDao: HistoryDao

    val userPreferences: UserPreferences

    val statisticsPreferences: StatisticsPreferences

    val okHttpClient: OkHttpClient

    val retrofit: Retrofit

    val application: Application

    val userRemoteDataSource: UserRemoteDataSource

    val storeDataSource: StoreDataSource

    val groceryDataSource: GroceryRemoteDataSource

    val statisticsDataSource: StatisticsRemoteDataSource

    val historyDataSource: HistoryRemoteDataSource

}