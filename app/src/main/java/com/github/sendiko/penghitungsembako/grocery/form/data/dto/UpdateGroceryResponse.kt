package com.github.sendiko.penghitungsembako.grocery.form.data.dto

import com.google.gson.annotations.SerializedName

data class UpdateGroceryResponse(

	@field:SerializedName("data")
	val groceryItem: GroceryItem,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)