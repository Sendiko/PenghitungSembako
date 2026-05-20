package id.my.sendiko.sembako.dashboard.data

import id.my.sendiko.sembako.core.domain.User
import id.my.sendiko.sembako.core.preferences.UserPreferences
import id.my.sendiko.sembako.dashboard.data.datasource.DashboardDataSource
import id.my.sendiko.sembako.dashboard.domain.DashboardRepository
import id.my.sendiko.sembako.dashboard.data.dto.SaveUserRequest
import id.my.sendiko.sembako.dashboard.data.dto.SaveUserResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume

class DashboardRepositoryImpl(
    val remoteDataSource: DashboardDataSource,
    val localDataSource: UserPreferences
) : DashboardRepository {

    override fun getUser(): Flow<User> {
        return localDataSource.getUser()
    }

    override suspend fun saveUserToRemote(user: User): Result<User> {
        return suspendCancellableCoroutine { continuation ->
            val request = SaveUserRequest(
                profileUrl = user.profileUrl,
                email = user.email,
                username = user.username
            )
            remoteDataSource.saveUser(request)
                .enqueue(
                    object : Callback<SaveUserResponse> {
                        override fun onResponse(
                            call: Call<SaveUserResponse?>,
                            response: Response<SaveUserResponse?>
                        ) {
                            when (response.code()) {
                                201 -> continuation
                                    .resume(Result.success(response.body()!!.userDto.toDomain()))

                                200 -> continuation
                                    .resume(Result.success(response.body()!!.userDto.toDomain()))

                                else -> continuation
                                    .resume(Result.failure(Exception(response.message())))
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

    override suspend fun saveUserToLocal(user: User): Result<Boolean> {
        return try {
            localDataSource.saveUser(user)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}