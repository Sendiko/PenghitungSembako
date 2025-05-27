package com.github.sendiko.penghitungsembako.login.data

import com.github.sendiko.penghitungsembako.core.domain.User
import com.github.sendiko.penghitungsembako.core.network.ApiService
import com.github.sendiko.penghitungsembako.core.preferences.UserPreferences
import com.github.sendiko.penghitungsembako.login.data.dto.SaveUserRequest
import com.github.sendiko.penghitungsembako.login.data.dto.SaveUserResponse
import com.github.sendiko.penghitungsembako.login.domain.LoginRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LoginRepositoryImpl(
    val remoteDataSource: ApiService,
    val localDataSource: UserPreferences
): LoginRepository {

    override suspend fun saveUserToRemote(user: User): Result<Boolean> {
        return suspendCoroutine { continuation ->
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
                            when(response.code()) {
                                201 -> continuation.resume(Result.success(true))
                                400 -> continuation.resume(Result.success(true)) // meaning the user data already exists
                                else -> continuation.resume(Result.failure(Exception(response.message())))
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