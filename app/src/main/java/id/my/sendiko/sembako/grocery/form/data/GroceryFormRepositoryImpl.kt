package id.my.sendiko.sembako.grocery.form.data

import id.my.sendiko.sembako.core.preferences.UserPreferences
import id.my.sendiko.sembako.grocery.core.data.GroceryDao
import id.my.sendiko.sembako.grocery.core.data.GroceryEntity
import id.my.sendiko.sembako.grocery.core.data.GroceryRemoteDataSource
import id.my.sendiko.sembako.grocery.core.domain.Grocery
import id.my.sendiko.sembako.grocery.form.data.dto.DeleteGroceryResponse
import id.my.sendiko.sembako.grocery.form.data.dto.GetGroceryResponse
import id.my.sendiko.sembako.grocery.form.data.dto.PostGroceryRequest
import id.my.sendiko.sembako.grocery.form.data.dto.PostGroceryResponse
import id.my.sendiko.sembako.grocery.form.data.dto.UpdateGroceryRequest
import id.my.sendiko.sembako.grocery.form.data.dto.UpdateGroceryResponse
import id.my.sendiko.sembako.grocery.form.domain.GroceryFormRepository
import id.my.sendiko.sembako.user.core.domain.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume

class GroceryFormRepositoryImpl(
    private val localDataSource: GroceryDao,
    private val remoteDataSource: GroceryRemoteDataSource,
    private val userPreferences: UserPreferences
) : GroceryFormRepository {
    override suspend fun saveGroceryToRemote(request: PostGroceryRequest): Result<Int> {
        return suspendCancellableCoroutine { continuation ->
            remoteDataSource.saveGrocery(
                userId = request.userId.toString().toRequestBody(),
                name = request.name.toRequestBody(),
                unit = request.unit.toRequestBody(),
                pricePerUnit = request.pricePerUnit.toString().toRequestBody(),
                image = request.image,
                storeId = request.storeId.toString().toRequestBody()
            ).enqueue(
                object : Callback<PostGroceryResponse> {
                    override fun onResponse(
                        call: Call<PostGroceryResponse?>,
                        response: Response<PostGroceryResponse?>
                    ) {
                        when (response.code()) {
                            201 -> continuation.resume(Result.success(201))
                            400 -> continuation.resume(Result.success(400))
                            else -> continuation.resume(Result.success(response.code()))
                        }
                    }

                    override fun onFailure(
                        call: Call<PostGroceryResponse?>,
                        response: Throwable
                    ) {
                        continuation.resume(Result.failure(Exception("Server Error.")))
                    }

                }
            )
        }
    }

    override suspend fun getGroceryFromRemote(id: Int): Result<Grocery> {
        return suspendCancellableCoroutine { continuation ->
            remoteDataSource.getGrocery(id).enqueue(
                object : Callback<GetGroceryResponse> {
                    override fun onResponse(
                        call: Call<GetGroceryResponse?>,
                        response: Response<GetGroceryResponse?>
                    ) {
                        when (response.code()) {
                            200 -> {
                                continuation.resume(
                                    Result.success(
                                        Grocery.convertFromResponse(
                                            response.body()!!.groceryItem
                                        )
                                    )
                                )
                            }

                            404 -> continuation.resume(Result.failure(Exception("Not Found.")))
                            else -> continuation.resume(Result.failure(Exception("Server Error.")))
                        }
                    }

                    override fun onFailure(
                        call: Call<GetGroceryResponse?>,
                        t: Throwable
                    ) {
                        continuation.resume(Result.failure(Exception("Server Error.")))
                    }

                }
            )
        }
    }

    override suspend fun updateGroceryToRemote(
        id: Int,
        request: UpdateGroceryRequest
    ): Result<Int> {
        return suspendCancellableCoroutine { continuation ->
            remoteDataSource.updateGrocery(
                id = id,
                name = request.name.toRequestBody(),
                unit = request.unit.toRequestBody(),
                pricePerUnit = request.pricePerUnit.toString().toRequestBody(),
                image = request.image
            ).enqueue(
                object : Callback<UpdateGroceryResponse> {
                    override fun onResponse(
                        call: Call<UpdateGroceryResponse?>,
                        response: Response<UpdateGroceryResponse?>
                    ) {
                        when (response.code()) {
                            200 -> continuation.resume(Result.success(200))
                            400 -> continuation.resume(Result.success(400))
                            else -> continuation.resume(Result.success(response.code()))
                        }
                    }

                    override fun onFailure(
                        call: Call<UpdateGroceryResponse?>,
                        response: Throwable
                    ) {
                        continuation.resume(Result.failure(Exception("Server Error.")))
                    }
                }
            )
        }
    }

    override suspend fun deleteGroceryFromRemote(id: Int): Result<Int> {
        return suspendCancellableCoroutine { continuation ->
            remoteDataSource.deleteGrocery(id)
                .enqueue(
                    object : Callback<DeleteGroceryResponse> {
                        override fun onResponse(
                            call: Call<DeleteGroceryResponse?>,
                            response: Response<DeleteGroceryResponse?>
                        ) {
                            when (response.code()) {
                                200 -> continuation.resume(Result.success(200))
                                404 -> continuation.resume(Result.success(404))
                                else -> continuation.resume(Result.success(response.code()))
                            }
                        }

                        override fun onFailure(
                            call: Call<DeleteGroceryResponse?>,
                            t: Throwable
                        ) {
                            TODO("Not yet implemented")
                        }

                    }
                )
        }
    }

    override suspend fun saveGroceryToLocal(grocery: GroceryEntity) {
        localDataSource.insert(grocery)
    }

    override fun getUser(): Flow<User> {
        return userPreferences.getUser()
    }


}