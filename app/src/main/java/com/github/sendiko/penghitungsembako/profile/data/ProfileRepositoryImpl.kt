package com.github.sendiko.penghitungsembako.profile.data

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.ClearCredentialException
import com.github.sendiko.penghitungsembako.core.preferences.UserPreferences
import com.github.sendiko.penghitungsembako.profile.domain.ProfileRepository
import com.github.sendiko.penghitungsembako.login.domain.User
import kotlinx.coroutines.flow.Flow

class ProfileRepositoryImpl(
    private val userPreferences: UserPreferences,
    private val context: Context
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
            val clearUser = User("", "", "")
            userPreferences.saveUser(clearUser)
            Result.success(true)
        } catch (e: ClearCredentialException) {
            Result.failure(e)
        }
    }
}