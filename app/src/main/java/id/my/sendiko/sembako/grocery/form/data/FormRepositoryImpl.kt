package id.my.sendiko.sembako.grocery.form.data

import android.util.Log
import id.my.sendiko.sembako.core.domain.User
import id.my.sendiko.sembako.core.network.ApiService
import id.my.sendiko.sembako.core.preferences.UserPreferences
import id.my.sendiko.sembako.grocery.core.data.GroceryDao
import id.my.sendiko.sembako.grocery.core.data.GroceryEntity
import id.my.sendiko.sembako.grocery.core.domain.Grocery
import id.my.sendiko.sembako.grocery.form.data.dto.DeleteGroceryResponse
import id.my.sendiko.sembako.grocery.form.data.dto.GetGroceryResponse
import id.my.sendiko.sembako.grocery.form.data.dto.PostGroceryRequest
import id.my.sendiko.sembako.grocery.form.data.dto.PostGroceryResponse
import id.my.sendiko.sembako.grocery.form.data.dto.UpdateGroceryRequest
import id.my.sendiko.sembako.grocery.form.data.dto.UpdateGroceryResponse
import id.my.sendiko.sembako.grocery.form.domain.FormRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FormRepositoryImpl(
    private val localDataSource: GroceryDao,
    private val remoteDataSource: ApiService,
    private val userPreferences: UserPreferences
): FormRepository {
    override suspend fun saveGroceryToRemote(request: PostGroceryRequest): Result<Int> {
        return suspendCoroutine { continuation ->
            Log.d("GROCERY", "saveGroceryToRemote: $request")
            remoteDataSource.saveGrocery(
                userId = request.userId.toString().toRequestBody(),
                name = request.name.toRequestBody(),
                unit = request.unit.toRequestBody(),
                pricePerUnit = request.pricePerUnit.toString().toRequestBody(),
                image = request.image
            ).enqueue(
                object : Callback<PostGroceryResponse> {
                    override fun onResponse(
                        call: Call<PostGroceryResponse?>,
                        response: Response<PostGroceryResponse?>
                    ) {
                        when(response.code()) {
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
        return suspendCoroutine { continuation ->
            remoteDataSource.getGrocery(id).enqueue(
                object : Callback<GetGroceryResponse> {
                    override fun onResponse(
                        call: Call<GetGroceryResponse?>,
                        response: Response<GetGroceryResponse?>
                    ) {
                        when(response.code()) {
                            200 -> {
                                continuation.resume(Result.success(Grocery.convertFromResponse(response.body()!!.groceryItem)))
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

    override suspend fun updateGroceryToRemote(id: Int, request: UpdateGroceryRequest): Result<Int> {
        return suspendCoroutine { continuation ->
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
                        when(response.code()) {
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
        return suspendCoroutine { continuation ->
            remoteDataSource.deleteGrocery(id)
                .enqueue(
                    object : Callback<DeleteGroceryResponse> {
                        override fun onResponse(
                            call: Call<DeleteGroceryResponse?>,
                            response: Response<DeleteGroceryResponse?>
                        ) {
                            when(response.code()) {
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