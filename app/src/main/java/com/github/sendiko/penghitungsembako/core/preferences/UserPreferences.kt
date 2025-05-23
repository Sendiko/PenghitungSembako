package com.github.sendiko.penghitungsembako.core.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.github.sendiko.penghitungsembako.user.domain.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(
    val dataStore: DataStore<Preferences>
) {

    private val uiModeKey = stringPreferencesKey("ui_mode")
    private val nameKey = stringPreferencesKey("name")
    private val emailKey = stringPreferencesKey("email")
    private val profileUrlKey = stringPreferencesKey("profile_url")

    suspend fun saveUser(user: User) {
        dataStore.edit { preferences ->
            preferences[nameKey] = user.username
            preferences[emailKey] = user.email
            preferences[profileUrlKey] = user.profileUrl
        }
    }

    fun getUser(): Flow<User> {
        return dataStore.data.map { preferences ->
            val name = preferences[nameKey] ?: ""
            val email = preferences[emailKey] ?: ""
            val profileUrl = preferences[profileUrlKey] ?: ""
            User(name, email, profileUrl)
        }
    }

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