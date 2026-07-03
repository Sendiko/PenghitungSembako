package id.my.sendiko.sembako.statistics.data.datasource

import id.my.sendiko.sembako.history.data.dto.GetHistoriesResponse
import id.my.sendiko.sembako.statistics.data.dto.GetStatisticsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface StatisticsRemoteDataSource {

    @GET("stats/{id}")
    fun getStatistics(
        @Path("id") id: Int
    ): Call<GetStatisticsResponse>

}