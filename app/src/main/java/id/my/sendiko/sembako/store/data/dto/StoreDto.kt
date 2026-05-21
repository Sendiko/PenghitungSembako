package id.my.sendiko.sembako.store.data.dto

import com.google.gson.annotations.SerializedName
import id.my.sendiko.sembako.store.domain.Store

data class StoreDto(

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("address")
    val address: String,

    @field:SerializedName("phone")
    val phone: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("userId")
    val userId: Int,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
){
    fun toDomain() = Store(id, name, address, phone, email)
}
