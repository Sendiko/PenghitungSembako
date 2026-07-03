package id.my.sendiko.sembako.statistics.data

import id.my.sendiko.sembako.core.preferences.StatisticsPreferences
import id.my.sendiko.sembako.core.preferences.UserPreferences
import id.my.sendiko.sembako.statistics.data.datasource.StatisticsRemoteDataSource
import id.my.sendiko.sembako.statistics.data.dto.GetStatisticsResponse
import id.my.sendiko.sembako.statistics.data.dto.StatisticsItem
import id.my.sendiko.sembako.statistics.domain.Statistics
import id.my.sendiko.sembako.statistics.domain.StatisticsRepository
import id.my.sendiko.sembako.store.core.data.datasource.StoreDataSource
import id.my.sendiko.sembako.store.core.data.dto.GetStoresResponse
import id.my.sendiko.sembako.store.core.domain.Store
import id.my.sendiko.sembako.user.core.domain.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume

class StatisticsRepositoryImpl(
    private val remoteDataSource: StatisticsRemoteDataSource,
    private val localDataSource: StatisticsPreferences,
    private val userPreferences: UserPreferences,
    private val storeRemoteDataSource: StoreDataSource
) : StatisticsRepository {

    override fun getUser(): Flow<User> {
        return userPreferences.getUser()
    }

    override suspend fun getStatisticsFromRemote(id: String): Result<StatisticsItem> {
        return suspendCancellableCoroutine { continuation ->
            remoteDataSource.getStatistics(id.toInt())
                .enqueue(
                    object : Callback<GetStatisticsResponse> {
                        override fun onResponse(
                            call: Call<GetStatisticsResponse?>,
                            response: Response<GetStatisticsResponse?>
                        ) {
                            when (response.code()) {
                                200 -> continuation.resume(Result.success(response.body()!!.statisticsItem))
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

    override suspend fun getStores(userId: Int): Result<List<Store>> {
        return suspendCancellableCoroutine { continuation ->
            storeRemoteDataSource.getStores(userId)
                .enqueue(
                    object : Callback<GetStoresResponse> {
                        override fun onResponse(
                            call: Call<GetStoresResponse?>,
                            response: Response<GetStoresResponse?>
                        ) {
                            when (response.code()) {
                                200 -> continuation.resume(Result.success(response.body()!!.stores.map { it.toDomain() }))
                                else -> continuation.resume(Result.failure(Exception("Server Error.")))
                            }
                        }

                        override fun onFailure(
                            call: Call<GetStoresResponse?>,
                            t: Throwable
                        ) {
                            continuation.resume(Result.failure(Exception("Server Error.")))
                        }

                    }
                )
        }
    }

    override suspend fun saveStatisticsToLocal(statistics: Statistics) {
        localDataSource.saveStatistic(statistics)
    }

    override fun getStatisticsFromLocal(): Flow<Statistics> {
        return localDataSource.getStatistics()
    }

}