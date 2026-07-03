package id.my.sendiko.sembako.history.data.datasource

import id.my.sendiko.sembako.history.data.dto.GetHistoriesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface HistoryRemoteDataSource {

    @GET("transaction/{id}")
    fun getHistories(
        @Path("id") id: String,
    ): Call<GetHistoriesResponse>

}