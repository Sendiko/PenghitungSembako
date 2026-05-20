package id.my.sendiko.sembako.history.domain

import id.my.sendiko.sembako.user.core.domain.User
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    fun getUser(): Flow<User>

    suspend fun getRemoteHistories(id: String): Result<List<History>>

    suspend fun getLocalHistories(): Flow<List<History>>

    suspend fun saveHistoriesToLocal(histories: List<History>)

}