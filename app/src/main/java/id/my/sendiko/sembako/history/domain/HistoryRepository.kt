package id.my.sendiko.sembako.history.domain

import id.my.sendiko.sembako.core.domain.User
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    fun getUser(): Flow<User>

    suspend fun getRemoteHistories(id: String): Result<List<History>>

    suspend fun getLocalHistories(): Flow<List<History>>

    suspend fun saveHistoriesToLocal(histories: List<History>)

}