package com.github.sendiko.penghitungsembako.user.data

import com.google.gson.annotations.SerializedName

data class GetUserResponse(

	@field:SerializedName("user")
	val userData: UserData,

	@field:SerializedName("status")
	val status: Int
)

data class UserData(

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
	val updatedAt: String
)
