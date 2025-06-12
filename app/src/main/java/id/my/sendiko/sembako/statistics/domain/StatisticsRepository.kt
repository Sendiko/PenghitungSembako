package id.my.sendiko.sembako.statistics.domain

import id.my.sendiko.sembako.core.domain.User
import id.my.sendiko.sembako.statistics.data.dto.StatisticsItem
import kotlinx.coroutines.flow.Flow

interface StatisticsRepository {
    fun getUser(): Flow<User>
    suspend fun getStatisticsFromRemote(id: String): Result<StatisticsItem>

    suspend fun saveStatisticsToLocal(statistics: Statistics)

    fun getStatisticsFromLocal(): Flow<Statistics>
}