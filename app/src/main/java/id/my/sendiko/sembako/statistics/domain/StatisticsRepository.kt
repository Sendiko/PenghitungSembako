package id.my.sendiko.sembako.statistics.domain

import id.my.sendiko.sembako.user.core.domain.User
import id.my.sendiko.sembako.statistics.data.dto.StatisticsItem
import id.my.sendiko.sembako.store.core.domain.Store
import kotlinx.coroutines.flow.Flow

interface StatisticsRepository {
    fun getUser(): Flow<User>
    suspend fun getStatisticsFromRemote(id: String): Result<StatisticsItem>

    suspend fun saveStatisticsToLocal(statistics: Statistics)

    fun getStatisticsFromLocal(): Flow<Statistics>
    suspend fun getStores(userId: Int): Result<List<Store>>
}