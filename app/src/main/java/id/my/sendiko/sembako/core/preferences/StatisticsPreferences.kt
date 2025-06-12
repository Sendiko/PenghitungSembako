package id.my.sendiko.sembako.core.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import id.my.sendiko.sembako.statistics.domain.Statistics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StatisticsPreferences(
    val dataStore: DataStore<Preferences>
) {

    private val totalIncome = intPreferencesKey("total_income")
    private val totalGroceries = intPreferencesKey("total_groceries")
    private val totalHistories = intPreferencesKey("total_histories")

    suspend fun saveStatistic(statistics: Statistics) {
        dataStore.edit { prefs ->
            prefs[totalIncome] = statistics.totalSales
            prefs[totalGroceries] = statistics.groceryCount
            prefs[totalHistories] = statistics.totalHistory
        }
    }

    fun getStatistics(): Flow<Statistics> {
        return dataStore.data.map { prefs ->
            Statistics(
                totalHistory = prefs[totalHistories] ?: 0,
                groceryCount = prefs[totalGroceries] ?: 0,
                totalSales = prefs[totalIncome] ?: 0
            )
        }
    }

}