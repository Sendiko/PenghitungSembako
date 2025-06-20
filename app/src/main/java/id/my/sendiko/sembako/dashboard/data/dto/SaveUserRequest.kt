package id.my.sendiko.sembako.dashboard.data.dto

import com.google.gson.annotations.SerializedName

data class SaveUserRequest(

	@field:SerializedName("profileUrl")
	val profileUrl: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String
)
