package com.github.sendiko.penghitungsembako.history.data.dto

import com.google.gson.annotations.SerializedName

data class GetHistoriesResponse(

	@field:SerializedName("history")
	val history: List<HistoryItem>,

	@field:SerializedName("status")
	val status: Int
)

data class Grocery(

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

data class HistoryItem(

	@field:SerializedName("Grocery")
	val grocery: Grocery,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("quantity")
	val quantity: Int,

	@field:SerializedName("totalPrice")
	val totalPrice: Int,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("userId")
	val userId: Int,

	@field:SerializedName("groceryId")
	val groceryId: Int,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
