package com.github.sendiko.penghitungsembako.profile.data

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.ClearCredentialException
import com.github.sendiko.penghitungsembako.core.preferences.UserPreferences
import com.github.sendiko.penghitungsembako.profile.domain.ProfileRepository
import com.github.sendiko.penghitungsembako.core.domain.User
import com.github.sendiko.penghitungsembako.core.network.ApiService
import com.github.sendiko.penghitungsembako.statistics.data.dto.GetStatisticsResponse
import com.github.sendiko.penghitungsembako.statistics.data.dto.Statistics
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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