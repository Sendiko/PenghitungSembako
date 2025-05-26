package com.github.sendiko.penghitungsembako.profile.domain

import com.github.sendiko.penghitungsembako.login.domain.User
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getUser(): Flow<User>

    suspend fun logout(): Result<Boolean>

}