package id.my.sendiko.sembako.user.profile.data

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.ClearCredentialException
import id.my.sendiko.sembako.core.preferences.UserPreferences
import id.my.sendiko.sembako.user.profile.domain.ProfileRepository
import id.my.sendiko.sembako.user.core.domain.User
import kotlinx.coroutines.flow.Flow

class ProfileRepositoryImpl(
    private val userLocalDataSource: UserPreferences,
    private val context: Context,
): ProfileRepository {

    override fun getUser(): Flow<User> {
        return userLocalDataSource.getUser()
    }

    override suspend fun logout(): Result<Boolean> {
        return try {
            val credentialManager = CredentialManager.create(context)
            credentialManager.clearCredentialState(
                ClearCredentialStateRequest()
            )
            val clearUser = User(0, "", "", "", false)
            userLocalDataSource.saveUser(clearUser)
            Result.success(true)
        } catch (e: ClearCredentialException) {
            Result.failure(e)
        }
    }

    override fun getDynamicTheme(): Flow<Boolean> {
        return userLocalDataSource.getDynamicTheme()
    }

    override suspend fun setDynamicTheme(dynamicTheme: Boolean) {
        userLocalDataSource.setDynamicTheme(dynamicTheme)
    }

}