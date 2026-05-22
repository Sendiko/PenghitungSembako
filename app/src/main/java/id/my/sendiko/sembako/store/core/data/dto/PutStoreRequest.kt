package id.my.sendiko.sembako.store.core.data.dto

import com.google.gson.annotations.SerializedName

data class PutStoreRequest(
    @field:SerializedName("address")
    val address: String,

    @field:SerializedName("phone")
    val phone: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String)
