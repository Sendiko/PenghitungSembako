package id.my.sendiko.sembako.store.core.data

import id.my.sendiko.sembako.core.preferences.UserPreferences
import id.my.sendiko.sembako.store.core.data.datasource.StoreDataSource
import id.my.sendiko.sembako.store.core.data.dto.GetStoresResponse
import id.my.sendiko.sembako.store.core.data.dto.PostStoreRequest
import id.my.sendiko.sembako.store.core.data.dto.PostStoreResponse
import id.my.sendiko.sembako.store.core.data.dto.PutStoreRequest
import id.my.sendiko.sembako.store.core.domain.Store
import id.my.sendiko.sembako.store.core.domain.StoreRepository
import id.my.sendiko.sembako.user.core.domain.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume

class StoreRepositoryImpl(
    private val dataSource: StoreDataSource,
    private val userLocalDataSource: UserPreferences
) : StoreRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getStores(userId: Int): Result<List<Store>> {
        return suspendCancellableCoroutine { continuation ->
            dataSource.getStores(userId)
                .enqueue(
                    object : Callback<GetStoresResponse> {
                        override fun onResponse(
                            call: Call<GetStoresResponse?>,
                            response: Response<GetStoresResponse?>
                        ) {
                            if (response.isSuccessful) {
                                val body = response.body()
                                if (body != null) {
                                    continuation.resume(Result.success(body.stores.map { it.toDomain() }))
                                    return
                                }
                                continuation.resume(Result.failure(Exception("Empty response body")))
                                return
                            }
                            continuation.resume(Result.failure(Exception("Error ${response.code()}: ${response.message()}")))
                        }

                        override fun onFailure(
                            call: Call<GetStoresResponse?>,
                            t: Throwable
                        ) {
                            continuation.resume(Result.failure(t))
                        }

                    }
                )
        }
    }

    override fun getUser(): Flow<User> {
        return userLocalDataSource.getUser()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun saveStore(store: Store): Result<Store> {
        val user = userLocalDataSource.getUser().first()
        val request = PostStoreRequest(
            name = store.name,
            address = store.address,
            phone = store.phone,
            email = store.email,
            userId = user.id
        )
        return suspendCancellableCoroutine { continuation ->
            dataSource.saveStore(request).enqueue(object : Callback<PostStoreResponse> {
                override fun onResponse(
                    call: Call<PostStoreResponse>,
                    response: Response<PostStoreResponse>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            continuation.resume(Result.success(body.storeDto.toDomain()))
                            return
                        }
                    }
                    continuation.resume(Result.failure(Exception("Gagal mendaftarkan toko.")))
                }

                override fun onFailure(call: Call<PostStoreResponse>, t: Throwable) {
                    continuation.resume(Result.failure(t))
                }
            })
        }
    }

    override suspend fun updateStore(id: Int, store: Store): Result<Store> {
        return suspendCancellableCoroutine { continuation ->
            val request = PutStoreRequest(
                address = store.address,
                phone = store.phone,
                name = store.name,
                email = store.email
            )
            dataSource.updateStore(id, request)
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
                                    return
                                }
                            }
                            continuation.resume(Result.failure(Exception("Gagal mengubah toko.")))
                        }

                        override fun onFailure(
                            call: Call<PostStoreResponse?>,
                            t: Throwable
                        ) {
                            continuation.resume(Result.failure(t))
                        }

                    }
                )
        }
    }

    override suspend fun deleteStore(id: Int): Result<Boolean> {
        return suspendCancellableCoroutine { continuation ->
            dataSource.deleteStore(id)
                .enqueue(
                    object : Callback<GetStoresResponse> {
                        override fun onResponse(
                            call: Call<GetStoresResponse?>,
                            response: Response<GetStoresResponse?>
                        ) {
                            if (response.isSuccessful) {
                                continuation.resume(Result.success(true))
                                return
                            }
                            continuation.resume(Result.failure(Exception("Gagal menghapus toko.")))
                        }

                        override fun onFailure(
                            call: Call<GetStoresResponse?>,
                            t: Throwable
                        ) {
                            continuation.resume(Result.failure(t))
                        }

                    }
                )
        }
    }
}