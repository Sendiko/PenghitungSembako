package com.github.sendiko.penghitungsembako.profile.domain

import android.content.Context
import com.github.sendiko.penghitungsembako.user.domain.User
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getUser(): Flow<User>

    suspend fun logout(): Result<Boolean>

}