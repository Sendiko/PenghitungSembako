package id.my.sendiko.sembako.dashboard.data.datasource

import id.my.sendiko.sembako.dashboard.data.dto.SaveUserRequest
import id.my.sendiko.sembako.dashboard.data.dto.SaveUserResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DashboardDataSource {

    @POST("user")
    fun saveUser(
        @Body request: SaveUserRequest
    ): Call<SaveUserResponse>

}