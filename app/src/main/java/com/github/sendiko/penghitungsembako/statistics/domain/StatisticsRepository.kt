package com.github.sendiko.penghitungsembako.statistics.domain

import com.github.sendiko.penghitungsembako.core.domain.User
import com.github.sendiko.penghitungsembako.statistics.data.dto.Statistics
import kotlinx.coroutines.flow.Flow

interface StatisticsRepository {
    fun getUser(): Flow<User>
    suspend fun getStatistics(id: String): Result<Statistics>
}