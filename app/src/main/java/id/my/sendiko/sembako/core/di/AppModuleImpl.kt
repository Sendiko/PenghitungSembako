package id.my.sendiko.sembako.core.di

import android.app.Application
import androidx.room.Room
import id.my.sendiko.sembako.core.database.AppDatabase
import id.my.sendiko.sembako.core.network.ApiService
import id.my.sendiko.sembako.core.network.BASE_URL
import id.my.sendiko.sembako.core.preferences.StatisticsPreferences
import id.my.sendiko.sembako.core.preferences.UserPreferences
import id.my.sendiko.sembako.core.preferences.dataStore
import id.my.sendiko.sembako.grocery.core.data.GroceryDao
import id.my.sendiko.sembako.history.data.HistoryDao
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

    override val sembakoDao: GroceryDao
        get() = database.sembakoDao

    override val historyDao: HistoryDao
        get() = database.historyDao

    override val userPreferences: UserPreferences
        get() = UserPreferences(app.applicationContext.dataStore)

    override val statisticsPreferences: StatisticsPreferences
        get() = StatisticsPreferences(app.applicationContext.dataStore)

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