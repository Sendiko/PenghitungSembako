package id.my.sendiko.sembako.core.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import id.my.sendiko.sembako.core.domain.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(
    val dataStore: DataStore<Preferences>
) {

    private val uiModeKey = stringPreferencesKey("ui_mode")
    private val userIdKey = intPreferencesKey("user")
    private val nameKey = stringPreferencesKey("name")
    private val emailKey = stringPreferencesKey("email")
    private val profileUrlKey = stringPreferencesKey("profile_url")
    private val dynamicThemeKey = booleanPreferencesKey("dynamic_theme")

    suspend fun setDynamicTheme(dynamicTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[dynamicThemeKey] = dynamicTheme
        }
    }

    fun getDynamicTheme(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[dynamicThemeKey] == true
        }
    }

    suspend fun saveUser(user: User) {
        dataStore.edit { preferences ->
            preferences[userIdKey] = user.id
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
            val id = preferences[userIdKey] ?: 0
            User(id, name, email, profileUrl)
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