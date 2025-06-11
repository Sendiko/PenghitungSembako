package id.my.sendiko.sembako.profile.data

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.ClearCredentialException
import id.my.sendiko.sembako.core.preferences.UserPreferences
import id.my.sendiko.sembako.profile.domain.ProfileRepository
import id.my.sendiko.sembako.core.domain.User
import kotlinx.coroutines.flow.Flow

class ProfileRepositoryImpl(
    private val userPreferences: UserPreferences,
    private val context: Context,
): ProfileRepository {

    override fun getUser(): Flow<User> {
        return userPreferences.getUser()
    }

    override suspend fun logout(): Result<Boolean> {
        return try {
            val credentialManager = CredentialManager.create(context)
            credentialManager.clearCredentialState(
                ClearCredentialStateRequest()
            )
            val clearUser = User(0, "", "", "")
            userPreferences.saveUser(clearUser)
            Result.success(true)
        } catch (e: ClearCredentialException) {
            Result.failure(e)
        }
    }

    override fun getDynamicTheme(): Flow<Boolean> {
        return userPreferences.getDynamicTheme()
    }

    override suspend fun setDynamicTheme(dynamicTheme: Boolean) {
        userPreferences.setDynamicTheme(dynamicTheme)
    }

}