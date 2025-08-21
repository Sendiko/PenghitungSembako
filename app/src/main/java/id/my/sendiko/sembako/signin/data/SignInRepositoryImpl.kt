package id.my.sendiko.sembako.signin.data

import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import id.my.sendiko.sembako.core.domain.User
import id.my.sendiko.sembako.core.network.ApiService
import id.my.sendiko.sembako.core.preferences.UserPreferences
import id.my.sendiko.sembako.dashboard.data.dto.SaveUserRequest
import id.my.sendiko.sembako.dashboard.data.dto.SaveUserResponse
import id.my.sendiko.sembako.signin.domain.SignInRepository
import kotlinx.coroutines.tasks.await
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SignInRepositoryImpl(
    private val auth: FirebaseAuth,
    private val remoteDataSource: ApiService,
    private val localDataSource: UserPreferences
) : SignInRepository {
    override suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser?> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            Result.success(authResult.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInAnonymously(): Result<FirebaseUser?> {
        return try {
            val authResult = auth.signInAnonymously().await()
            Result.success(authResult.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveUserToRemote(user: User): Result<User> {
        return suspendCoroutine { continuation ->
            val request = SaveUserRequest(
                profileUrl = user.profileUrl.toString(),
                email = user.email?:"",
                username = user.username?:""
            )
            remoteDataSource.saveUser(request)
                .enqueue(
                    object : Callback<SaveUserResponse> {
                        override fun onResponse(
                            call: Call<SaveUserResponse?>,
                            response: Response<SaveUserResponse?>
                        ) {
                            when(response.code()) {
                                201 -> {
                                    val result = User(
                                        id = response.body()!!.user.id.toString(),
                                        username = response.body()!!.user.username,
                                        email = response.body()!!.user.email,
                                        profileUrl = response.body()!!.user.profileUrl.toUri()
                                    )
                                    continuation.resume(Result.success(result))
                                }
                                200 -> {
                                    /* Meaning the user data already exists */
                                    val result = User(
                                        id = response.body()!!.user.id.toString(),
                                        username = response.body()!!.user.username,
                                        email = response.body()!!.user.email,
                                        profileUrl = response.body()!!.user.profileUrl.toUri()
                                    )
                                    continuation.resume(Result.success(result))
                                }
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