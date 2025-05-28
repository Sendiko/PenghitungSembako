package com.github.sendiko.penghitungsembako.core.network

import com.github.sendiko.penghitungsembako.grocery.dashboard.data.dto.GetGroceriesResponse
import com.github.sendiko.penghitungsembako.login.data.dto.SaveUserRequest
import com.github.sendiko.penghitungsembako.login.data.dto.SaveUserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("user")
    fun saveUser(
        @Body request: SaveUserRequest
    ): Call<SaveUserResponse>

    @GET("grocery/{userId}")
    fun getGroceries(
        @Path("userId") userId: String,
    ): Call<GetGroceriesResponse>

}