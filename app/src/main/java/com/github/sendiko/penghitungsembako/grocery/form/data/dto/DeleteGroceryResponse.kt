package com.github.sendiko.penghitungsembako.grocery.form.data.dto

import com.google.gson.annotations.SerializedName

data class DeleteGroceryResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)
