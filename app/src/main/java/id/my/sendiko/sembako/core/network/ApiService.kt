package id.my.sendiko.sembako.core.network

import id.my.sendiko.sembako.history.data.dto.GetHistoriesResponse
import id.my.sendiko.sembako.statistics.data.dto.GetStatisticsResponse
import retrofit2.Call
import retrofit2.http.GET
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