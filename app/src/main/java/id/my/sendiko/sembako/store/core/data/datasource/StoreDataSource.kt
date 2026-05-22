package id.my.sendiko.sembako.store.core.data.datasource

import id.my.sendiko.sembako.store.core.data.dto.GetStoresResponse
import id.my.sendiko.sembako.store.core.data.dto.PostStoreRequest
import id.my.sendiko.sembako.store.core.data.dto.PostStoreResponse
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