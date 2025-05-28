package com.github.sendiko.penghitungsembako.grocery.form.data

import com.google.gson.annotations.SerializedName

data class PostGroceryResponse(

	@field:SerializedName("grocery")
	val grocery: GroceryItem? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class GroceryItem(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("unit")
	val unit: String? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
