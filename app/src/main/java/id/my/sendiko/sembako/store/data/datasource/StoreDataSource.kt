package id.my.sendiko.sembako.store.data.datasource

import id.my.sendiko.sembako.store.data.dto.GetStoresResponse
import id.my.sendiko.sembako.store.data.dto.PostStoreRequest
import id.my.sendiko.sembako.store.data.dto.PostStoreResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface StoreDataSource {

    @POST("store")
    fun saveStore(
        @Body request: PostStoreRequest
    ): Call<PostStoreResponse>

    @GET("store/{id}")
    fun getStores(
        @Path("id") id: Int
    ): Call<GetStoresResponse>

}