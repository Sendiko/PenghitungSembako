package com.github.sendiko.penghitungsembako.grocery.dashboard.data

import com.github.sendiko.penghitungsembako.core.domain.User
import com.github.sendiko.penghitungsembako.core.network.ApiService
import com.github.sendiko.penghitungsembako.core.preferences.UiMode
import com.github.sendiko.penghitungsembako.core.preferences.UserPreferences
import com.github.sendiko.penghitungsembako.grocery.core.data.GroceryDao
import com.github.sendiko.penghitungsembako.grocery.core.domain.Grocery
import com.github.sendiko.penghitungsembako.grocery.dashboard.data.dto.GetGroceriesResponse
import com.github.sendiko.penghitungsembako.grocery.dashboard.domain.DashboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DashboardRepositoryImpl(
    private val prefs: UserPreferences,
    private val localDataSource: GroceryDao,
    private val remoteDataSource: ApiService
) : DashboardRepository {

    override fun getUser(): Flow<User> {
        return prefs.getUser()
    }

    override suspend fun getRemoteGroceries(userId: String): Result<List<Grocery>> {
        return suspendCoroutine { continuation ->
            remoteDataSource.getGroceries(userId)
                .enqueue(
                    object : Callback<GetGroceriesResponse> {
                        override fun onResponse(
                            call: Call<GetGroceriesResponse?>,
                            response: Response<GetGroceriesResponse?>
                        ) {
                            when(response.code()) {
                                200 -> {
                                    val result = response.body()!!.groceries.map {
                                        Grocery(
                                            id = it.id,
                                            name = it.name,
                                            unit = it.unit,
                                            pricePerUnit = it.price,
                                            imageUrl = it.imageUrl
                                        )
                                    }
                                    continuation.resume(Result.success(result))
                                }
                                400 -> continuation.resume(Result.failure(Exception("Bad Request")))
                                else -> continuation.resume(Result.failure(Exception("Server error.")))
                            }
                        }

                        override fun onFailure(
                            call: Call<GetGroceriesResponse?>,
                            t: Throwable
                        ) {
                            continuation.resume(Result.failure(Exception("Server error.")))
                        }
                    }
                )
        }
    }

    override suspend fun getLocalGroceries(): Flow<List<Grocery>> {
        val result = localDataSource.getAll().map {
            it.map {
                Grocery(
                    id = it.id,
                    name = it.name,
                    unit = it.unit,
                    pricePerUnit = it.pricePerUnit.toInt(),
                    imageUrl = it.imageUrl,
                )
            }
        }
        return result
    }

    override suspend fun setUiMode(uiMode: UiMode) {
        prefs.setUiMode(uiMode)
    }

    override fun getUiMode(): Flow<UiMode> {
        return prefs.getUiMode()
    }


}