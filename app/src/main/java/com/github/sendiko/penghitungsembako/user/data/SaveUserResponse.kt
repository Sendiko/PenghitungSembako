package com.github.sendiko.penghitungsembako.user.data

import com.google.gson.annotations.SerializedName

data class SaveUserResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)
