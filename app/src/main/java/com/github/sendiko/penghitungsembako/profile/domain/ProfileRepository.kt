package com.github.sendiko.penghitungsembako.profile.domain

import com.github.sendiko.penghitungsembako.core.domain.User
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getUser(): Flow<User>

    suspend fun logout(): Result<Boolean>

    fun getDynamicTheme(): Flow<Boolean>

    suspend fun setDynamicTheme(dynamicTheme: Boolean)

}