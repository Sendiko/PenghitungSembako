package id.my.sendiko.sembako.user.core.data.dto

import com.google.gson.annotations.SerializedName
import id.my.sendiko.sembako.store.core.data.dto.StoreDto
import id.my.sendiko.sembako.user.core.domain.User

data class SaveUserResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("user")
    val userDto: UserDto,

    @field:SerializedName("status")
    val status: Int
)

data class UserDto(

    @field:SerializedName("profileUrl")
    val profileUrl: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String,

    @field:SerializedName("Stores")
    val stores: List<StoreDto>
) {
    fun toDomain() = User(id, username, email, profileUrl, stores.isNotEmpty())
}
