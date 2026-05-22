package id.my.sendiko.sembako.store.core.data

import id.my.sendiko.sembako.core.preferences.UserPreferences
import id.my.sendiko.sembako.store.core.data.datasource.StoreDataSource
import id.my.sendiko.sembako.store.core.data.dto.GetStoresResponse
import id.my.sendiko.sembako.store.core.domain.Store
import id.my.sendiko.sembako.store.core.domain.StoreRepository
import id.my.sendiko.sembako.user.core.domain.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
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
}