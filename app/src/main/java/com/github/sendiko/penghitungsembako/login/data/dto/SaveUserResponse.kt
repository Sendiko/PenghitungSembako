package com.github.sendiko.penghitungsembako.login.data.dto

import com.google.gson.annotations.SerializedName

data class SaveUserResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)
