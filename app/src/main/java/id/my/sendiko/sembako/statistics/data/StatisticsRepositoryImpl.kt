package id.my.sendiko.sembako.statistics.data

import id.my.sendiko.sembako.core.domain.User
import id.my.sendiko.sembako.core.network.ApiService
import id.my.sendiko.sembako.core.preferences.UserPreferences
import id.my.sendiko.sembako.statistics.data.dto.GetStatisticsResponse
import id.my.sendiko.sembako.statistics.data.dto.Statistics
import id.my.sendiko.sembako.statistics.domain.StatisticsRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class StatisticsRepositoryImpl(
    private val remoteDataSource: ApiService,
    private val userPreferences: UserPreferences
): StatisticsRepository {
    override fun getUser(): Flow<User> {
        return userPreferences.getUser()
    }

    override suspend fun getStatistics(id: String): Result<Statistics> {
        return suspendCoroutine { continuation ->
            remoteDataSource.getStatistics(id.toInt())
                .enqueue(
                    object : Callback<GetStatisticsResponse> {
                        override fun onResponse(
                            call: Call<GetStatisticsResponse?>,
                            response: Response<GetStatisticsResponse?>
                        ) {
                            when(response.code()) {
                                200 -> continuation.resume(Result.success(response.body()!!.statistics))
                                404 -> continuation.resume(Result.failure(Exception("Not Found.")))
                                else -> continuation.resume(Result.failure(Exception("Server Error.")))
                            }
                        }

                        override fun onFailure(
                            call: Call<GetStatisticsResponse?>,
                            t: Throwable
                        ) {
                            continuation.resume(Result.failure(Exception("Server Error.")))
                        }

                    }
                )
        }
    }

}