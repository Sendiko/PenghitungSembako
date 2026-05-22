package id.my.sendiko.sembako.store.core.data.dto

import com.google.gson.annotations.SerializedName

data class PostStoreRequest(

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("userId")
	val userId: Int,

	@field:SerializedName("email")
	val email: String
)