package id.my.sendiko.sembako.core.network

import id.my.sendiko.sembako.grocery.list.data.dto.GetGroceriesResponse
import id.my.sendiko.sembako.grocery.form.data.dto.DeleteGroceryResponse
import id.my.sendiko.sembako.grocery.form.data.dto.GetGroceryResponse
import id.my.sendiko.sembako.grocery.form.data.dto.PostGroceryResponse
import id.my.sendiko.sembako.grocery.form.data.dto.UpdateGroceryResponse
import id.my.sendiko.sembako.grocery.list.data.dto.SaveTransactionRequest
import id.my.sendiko.sembako.history.data.dto.GetHistoriesResponse
import id.my.sendiko.sembako.grocery.list.data.dto.SaveTransactionResponse
import id.my.sendiko.sembako.statistics.data.dto.GetStatisticsResponse
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

    @GET("stats/{id}")
    fun getStatistics(
        @Path("id") id: Int
    ): Call<GetStatisticsResponse>

    @GET("transaction/{id}")
    fun getHistories(
        @Path("id") id: String,
    ): Call<GetHistoriesResponse>

}