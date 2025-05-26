package com.github.sendiko.penghitungsembako.login.data

import com.google.gson.annotations.SerializedName

data class SaveUserRequest(

	@field:SerializedName("profileUrl")
	val profileUrl: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String
)
