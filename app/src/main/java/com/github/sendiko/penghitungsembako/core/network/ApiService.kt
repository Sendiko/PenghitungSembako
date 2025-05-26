package com.github.sendiko.penghitungsembako.core.network

import com.github.sendiko.penghitungsembako.login.data.GetUserResponse
import com.github.sendiko.penghitungsembako.login.data.SaveUserRequest
import com.github.sendiko.penghitungsembako.login.data.SaveUserResponse
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

    @GET("user/{id}")
    fun getUser(
        @Path("id") id: String,
    ): Call<GetUserResponse>

}