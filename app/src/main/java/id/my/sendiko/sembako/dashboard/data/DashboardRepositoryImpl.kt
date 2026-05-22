package id.my.sendiko.sembako.dashboard.data

import id.my.sendiko.sembako.core.preferences.UserPreferences
import id.my.sendiko.sembako.dashboard.domain.DashboardRepository
import id.my.sendiko.sembako.store.core.data.datasource.StoreDataSource
import id.my.sendiko.sembako.store.core.data.dto.PostStoreRequest
import id.my.sendiko.sembako.store.core.data.dto.PostStoreResponse
import id.my.sendiko.sembako.store.core.domain.Store
import id.my.sendiko.sembako.user.core.data.UserRemoteDataSource
import id.my.sendiko.sembako.user.core.data.dto.SaveUserRequest
import id.my.sendiko.sembako.user.core.data.dto.SaveUserResponse
import id.my.sendiko.sembako.user.core.domain.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume

class DashboardRepositoryImpl(
    val userRemoteDataSource: UserRemoteDataSource,
    val userLocalDataSource: UserPreferences,
    val storeRemoteDataSource: StoreDataSource,
) : DashboardRepository {

    override fun getUser(): Flow<User> {
        return userLocalDataSource.getUser()
    }

    override suspend fun saveUserToRemote(user: User): Result<User> {
        return suspendCancellableCoroutine { continuation ->
            val request = SaveUserRequest(
                profileUrl = user.profileUrl,
                email = user.email,
                username = user.username
            )
            userRemoteDataSource.saveUser(request)
                .enqueue(
                    object : Callback<SaveUserResponse> {
                        override fun onResponse(
                            call: Call<SaveUserResponse?>,
                            response: Response<SaveUserResponse?>
                        ) {
                            if (response.isSuccessful) {
                                val body = response.body()
                                if (body != null) {
                                    continuation.resume(Result.success(body.userDto.toDomain()))
                                } else {
                                    continuation.resume(Result.failure(Exception("Empty response body")))
                                }
                            } else {
                                continuation.resume(Result.failure(Exception("Error ${response.code()}: ${response.message()}")))
                            }
                        }

                        override fun onFailure(
                            call: Call<SaveUserResponse?>,
                            t: Throwable
                        ) {
                            continuation.resume(Result.failure(t))
                        }

                    }
                )
        }
    }

    override suspend fun saveStore(store: Store): Result<Store> {
        val user = getUser().first()
        return suspendCancellableCoroutine { continuation ->
            val request = PostStoreRequest(
                address = store.address,
                phone = store.phone,
                name = store.name,
                userId = user.id,
                email = user.email
            )
            storeRemoteDataSource.saveStore(request)
                .enqueue(
                    object : Callback<PostStoreResponse> {
                        override fun onResponse(
                            call: Call<PostStoreResponse?>,
                            response: Response<PostStoreResponse?>
                        ) {
                            if (response.isSuccessful) {
                                val body = response.body()
                                if (body != null) {
                                    continuation.resume(Result.success(body.storeDto.toDomain()))
                                } else {
                                    continuation.resume(Result.failure(Exception("Empty response body")))
                                }
                            } else {
                                continuation.resume(Result.failure(Exception("Error ${response.code()}: ${response.message()}")))
                            }
                        }

                        override fun onFailure(
                            call: Call<PostStoreResponse?>,
                            t: Throwable
                        ) {
                            continuation.resume(Result.failure(Exception(t)))
                        }

                    }
                )
        }
    }

    override suspend fun setHasStore() {
        userLocalDataSource.setHasStore()
    }

    override suspend fun saveUserToLocal(user: User): Result<Boolean> {
        return try {
            userLocalDataSource.saveUser(user)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}