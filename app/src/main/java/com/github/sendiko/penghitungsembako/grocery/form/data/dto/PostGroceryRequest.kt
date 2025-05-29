package com.github.sendiko.penghitungsembako.grocery.form.data.dto

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class PostGroceryRequest(

    @field:SerializedName("userId")
    val userId: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("unit")
    val unit: String,

    @field:SerializedName("price")
    val pricePerUnit: Double,

    @field:SerializedName("image")
    val image: MultipartBody.Part,
)