package id.my.sendiko.sembako.grocery.core.data

import id.my.sendiko.sembako.grocery.form.data.dto.DeleteGroceryResponse
import id.my.sendiko.sembako.grocery.form.data.dto.GetGroceryResponse
import id.my.sendiko.sembako.grocery.form.data.dto.PostGroceryResponse
import id.my.sendiko.sembako.grocery.form.data.dto.UpdateGroceryResponse
import id.my.sendiko.sembako.grocery.list.data.dto.GetGroceriesResponse
import id.my.sendiko.sembako.grocery.list.data.dto.SaveTransactionRequest
import id.my.sendiko.sembako.grocery.list.data.dto.SaveTransactionResponse
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

interface GroceryRemoteDataSource {

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
        @Part("storeId") storeId: RequestBody
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

    @POST("transaction")
    fun saveHistory(
        @Body request: SaveTransactionRequest
    ): Call<SaveTransactionResponse>

}