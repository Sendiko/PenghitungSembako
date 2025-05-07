package com.github.sendiko.penghitungsembako.core.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(
    val dataStore: DataStore<Preferences>
) {

    val uiModeKey = stringPreferencesKey("ui_mode")

    suspend fun setUiMode(uiMode: UiMode) {
        dataStore.edit { preferences ->
            preferences[uiModeKey] = uiMode.name
        }
    }

    fun getUiMode(): Flow<UiMode> {
        return dataStore.data.map { preferences ->
            val uiModeName = preferences[uiModeKey] ?: UiMode.GRID.name
            UiMode.valueOf(uiModeName)
        }
    }

}