package id.my.sendiko.sembako.user.core.data

import id.my.sendiko.sembako.user.core.data.dto.SaveUserRequest
import id.my.sendiko.sembako.user.core.data.dto.SaveUserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserRemoteDataSource {

    @POST("user")
    fun saveUser(
        @Body request: SaveUserRequest
    ): Call<SaveUserResponse>

}