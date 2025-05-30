package com.github.sendiko.penghitungsembako.profile.domain

import com.github.sendiko.penghitungsembako.core.domain.User
import com.github.sendiko.penghitungsembako.profile.data.dto.GetStatisticsResponse
import com.github.sendiko.penghitungsembako.profile.data.dto.Statistics
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getUser(): Flow<User>

    suspend fun logout(): Result<Boolean>

    fun getDynamicTheme(): Flow<Boolean>

    suspend fun setDynamicTheme(dynamicTheme: Boolean)

    suspend fun getStatistics(id: String): Result<Statistics>

}