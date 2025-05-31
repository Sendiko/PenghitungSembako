package com.github.sendiko.penghitungsembako.core.network

import com.github.sendiko.penghitungsembako.grocery.list.data.dto.GetGroceriesResponse
import com.github.sendiko.penghitungsembako.grocery.form.data.dto.DeleteGroceryResponse
import com.github.sendiko.penghitungsembako.grocery.form.data.dto.GetGroceryResponse
import com.github.sendiko.penghitungsembako.grocery.form.data.dto.PostGroceryResponse
import com.github.sendiko.penghitungsembako.grocery.form.data.dto.UpdateGroceryResponse
import com.github.sendiko.penghitungsembako.grocery.list.data.dto.SaveTransactionRequest
import com.github.sendiko.penghitungsembako.history.data.dto.GetHistoriesResponse
import com.github.sendiko.penghitungsembako.grocery.list.data.dto.SaveTransactionResponse
import com.github.sendiko.penghitungsembako.login.data.dto.SaveUserRequest
import com.github.sendiko.penghitungsembako.login.data.dto.SaveUserResponse
import com.github.sendiko.penghitungsembako.statistics.data.dto.GetStatisticsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @Multipart
    @POST("grocery")
    fun saveGrocery(
        @Part("userId") userId: RequestBody,
        @Part("name") name: RequestBody,
        @Part("unit") unit: RequestBody,
        @Part("price") pricePerUnit: RequestBody,
        @Part image: MultipartBody.Part,
    ): Call<PostGroceryResponse>

    @Multipart
    @PUT("grocery/{id}")
    fun updateGrocery(
        @Path("id") id: Int,
        @Part("name") name: RequestBody,
        @Part("unit") unit: RequestBody,
        @Part("price") pricePerUnit: RequestBody,
        @Part image: MultipartBody.Part? = null,
    ): Call<UpdateGroceryResponse>

    @GET("grocery/details/{id}")
    fun getGrocery(
        @Path("id") id: Int,
    ): Call<GetGroceryResponse>

    @DELETE("grocery/{id}")
    fun deleteGrocery(
        @Path("id") id: Int,
    ): Call<DeleteGroceryResponse>

    @GET("stats/{id}")
    fun getStatistics(
        @Path("id") id: Int
    ): Call<GetStatisticsResponse>

    @GET("history/{id}")
    fun getHistories(
        @Path("id") id: String,
    ): Call<GetHistoriesResponse>

    @POST("history")
    fun saveHistory(
        @Body request: SaveTransactionRequest
    ): Call<SaveTransactionResponse>

}