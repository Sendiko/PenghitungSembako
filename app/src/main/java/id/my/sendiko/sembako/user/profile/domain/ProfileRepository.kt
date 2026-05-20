package id.my.sendiko.sembako.user.profile.domain

import id.my.sendiko.sembako.user.core.domain.User
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getUser(): Flow<User>

    suspend fun logout(): Result<Boolean>

    fun getDynamicTheme(): Flow<Boolean>

    suspend fun setDynamicTheme(dynamicTheme: Boolean)

}