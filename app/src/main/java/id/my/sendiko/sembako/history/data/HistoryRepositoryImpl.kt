package id.my.sendiko.sembako.history.data

import id.my.sendiko.sembako.core.domain.User
import id.my.sendiko.sembako.core.network.ApiService
import id.my.sendiko.sembako.core.preferences.UserPreferences
import id.my.sendiko.sembako.history.data.dto.GetHistoriesResponse
import id.my.sendiko.sembako.history.domain.History
import id.my.sendiko.sembako.history.domain.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class HistoryRepositoryImpl(
    private val userPreferences: UserPreferences,
    private val remoteDataSource: ApiService,
    private val localDataSource: HistoryDao
): HistoryRepository {
    override fun getUser(): Flow<User> {
        return userPreferences.getUser()
    }

    override suspend fun getRemoteHistories(id: String): Result<List<History>> {
        return suspendCoroutine { continuation ->
            remoteDataSource.getHistories(id)
                .enqueue(
                    object : Callback<GetHistoriesResponse> {
                        override fun onResponse(
                            call: Call<GetHistoriesResponse?>,
                            response: Response<GetHistoriesResponse?>
                        ) {
                            when(response.code()) {
                                200 -> {
                                    continuation.resume(Result.success(
                                        response.body()?.history?.map {
                                            History.fromHistoryItem(it)
                                        } ?: emptyList()
                                    ))
                                }
                                else -> continuation.resume(Result.failure(Exception("Server Error.")))
                            }
                        }

                        override fun onFailure(
                            call: Call<GetHistoriesResponse?>,
                            t: Throwable
                        ) {
                            continuation.resume(Result.failure(Exception("Server Error. Entering Offline Mode.")))
                        }

                    }
                )
        }
    }

    override suspend fun getLocalHistories(): Flow<List<History>> {
        val result = localDataSource.getHistories().map { histories ->
            histories.map {
                History.fromHistoryEntity(it)
            }
        }
        return result
    }

    override suspend fun saveHistoriesToLocal(histories: List<History>){
        localDataSource.deleteAllHistories()
        histories.forEach { history ->
            localDataSource.insertHistory(history.toHistoryEntity())
        }
    }
}