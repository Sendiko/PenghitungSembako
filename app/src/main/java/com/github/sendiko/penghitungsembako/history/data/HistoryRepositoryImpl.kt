package com.github.sendiko.penghitungsembako.history.data

import com.github.sendiko.penghitungsembako.core.domain.User
import com.github.sendiko.penghitungsembako.core.network.ApiService
import com.github.sendiko.penghitungsembako.core.preferences.UserPreferences
import com.github.sendiko.penghitungsembako.history.data.dto.GetHistoriesResponse
import com.github.sendiko.penghitungsembako.history.domain.History
import com.github.sendiko.penghitungsembako.history.domain.HistoryRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class HistoryRepositoryImpl(
    private val userPreferences: UserPreferences,
    private val remoteDataSource: ApiService
): HistoryRepository {
    override fun getUser(): Flow<User> {
        return userPreferences.getUser()
    }

    override suspend fun getHistory(id: String): Result<List<History>> {
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
                            continuation.resume(Result.failure(Exception("Server Error.")))
                        }

                    }
                )
        }
    }
}