package com.github.sendiko.penghitungsembako.grocery.dashboard.data.dto

import com.google.gson.annotations.SerializedName

data class GetGroceriesResponse(

	@field:SerializedName("groceries")
	val groceries: List<GroceriesItem>,

	@field:SerializedName("status")
	val status: Int
)

data class GroceriesItem(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("imageUrl")
	val imageUrl: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("userId")
	val userId: Int,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
