package id.my.sendiko.sembako.store.core.data.datasource

import androidx.room.Delete
import id.my.sendiko.sembako.store.core.data.dto.GetStoresResponse
import id.my.sendiko.sembako.store.core.data.dto.PostStoreRequest
import id.my.sendiko.sembako.store.core.data.dto.PostStoreResponse
import id.my.sendiko.sembako.store.core.data.dto.PutStoreRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @PUT("store/{id}")
    fun updateStore(
        @Path("id") id: Int,
        @Body request: PutStoreRequest
    ): Call<PostStoreResponse>

    @DELETE("store/{id}")
    fun deleteStore(
        @Path("id") id: Int
    ): Call<GetStoresResponse>

}