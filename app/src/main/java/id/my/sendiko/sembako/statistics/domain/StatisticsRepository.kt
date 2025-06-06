package id.my.sendiko.sembako.statistics.domain

import id.my.sendiko.sembako.core.domain.User
import id.my.sendiko.sembako.statistics.data.dto.Statistics
import kotlinx.coroutines.flow.Flow

interface StatisticsRepository {
    fun getUser(): Flow<User>
    suspend fun getStatistics(id: String): Result<Statistics>
}