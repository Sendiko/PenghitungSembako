package id.my.sendiko.sembako.profile.domain

import id.my.sendiko.sembako.core.domain.User
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getUser(): Flow<User>

    suspend fun logout(): Result<Boolean>

    fun getDynamicTheme(): Flow<Boolean>

    suspend fun setDynamicTheme(dynamicTheme: Boolean)

}