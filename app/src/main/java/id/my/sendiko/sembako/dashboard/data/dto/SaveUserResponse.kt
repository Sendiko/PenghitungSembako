package id.my.sendiko.sembako.dashboard.data.dto

import com.google.gson.annotations.SerializedName
import id.my.sendiko.sembako.core.domain.Store
import id.my.sendiko.sembako.core.domain.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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

@Serializable
data class StoreDto(

	@SerialName("createdAt")
	val createdAt: String,

	@SerialName("address")
	val address: String,

	@SerialName("phone")
	val phone: String,

	@SerialName("name")
	val name: String,

	@SerialName("id")
	val id: Int,

	@SerialName("userId")
	val userId: Int,

	@SerialName("email")
	val email: String,

	@SerialName("updatedAt")
	val updatedAt: String
){
	fun toDomain() = Store(id, name, address, phone, email)
}
