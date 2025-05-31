package com.github.sendiko.penghitungsembako.grocery.list.data

import com.github.sendiko.penghitungsembako.core.domain.User
import com.github.sendiko.penghitungsembako.core.network.ApiService
import com.github.sendiko.penghitungsembako.core.preferences.UiMode
import com.github.sendiko.penghitungsembako.core.preferences.UserPreferences
import com.github.sendiko.penghitungsembako.grocery.core.data.GroceryDao
import com.github.sendiko.penghitungsembako.grocery.core.data.GroceryEntity
import com.github.sendiko.penghitungsembako.grocery.core.domain.Grocery
import com.github.sendiko.penghitungsembako.grocery.list.data.dto.GetGroceriesResponse
import com.github.sendiko.penghitungsembako.grocery.list.data.dto.SaveTransactionRequest
import com.github.sendiko.penghitungsembako.grocery.list.data.dto.SaveTransactionResponse
import com.github.sendiko.penghitungsembako.grocery.list.domain.ListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.forEach
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ListRepositoryImpl(
    private val prefs: UserPreferences,
    private val localDataSource: GroceryDao,
    private val remoteDataSource: ApiService
) : ListRepository {

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
                            when (response.code()) {
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

    override suspend fun saveGroceries(groceries: List<Grocery>): Result<Boolean> {
        return try {
            localDataSource.deleteAll()

            groceries.forEach { grocery ->
                val item = GroceryEntity(
                    name = grocery.name,
                    unit = grocery.unit,
                    pricePerUnit = grocery.pricePerUnit.toDouble(),
                    imageUrl = grocery.imageUrl,
                    remoteId = grocery.id.toString()
                )
                localDataSource.insert(item)
            }
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun setUiMode(uiMode: UiMode) {
        prefs.setUiMode(uiMode)
    }

    override fun getUiMode(): Flow<UiMode> {
        return prefs.getUiMode()
    }

    override suspend fun saveTransaction(request: SaveTransactionRequest): Result<Int> {
        return suspendCoroutine { continuation ->
            remoteDataSource.saveHistory(request)
                .enqueue(
                    object : Callback<SaveTransactionResponse> {
                        override fun onResponse(
                            call: Call<SaveTransactionResponse?>,
                            response: Response<SaveTransactionResponse?>
                        ) {
                            when (response.code()) {
                                201 -> continuation.resume(Result.success(200))
                                else -> continuation.resume(Result.failure(Exception("Server Error.")))
                            }
                        }

                        override fun onFailure(
                            call: Call<SaveTransactionResponse?>,
                            t: Throwable
                        ) {
                            continuation.resume(Result.failure(Exception("Server Error.")))
                        }
                    }
                )
        }
    }

}