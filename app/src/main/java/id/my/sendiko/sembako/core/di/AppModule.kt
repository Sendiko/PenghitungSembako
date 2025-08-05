package id.my.sendiko.sembako.core.di

import android.app.Application
import id.my.sendiko.sembako.core.network.ApiService
import id.my.sendiko.sembako.core.preferences.StatisticsPreferences
import id.my.sendiko.sembako.core.preferences.UserPreferences
import id.my.sendiko.sembako.grocery.core.data.GroceryDao
import id.my.sendiko.sembako.history.data.HistoryDao

interface AppModule {

    val sembakoDao: GroceryDao

    val historyDao: HistoryDao

    val userPreferences: UserPreferences

    val statisticsPreferences: StatisticsPreferences

    val apiService: ApiService

    val application: Application

}