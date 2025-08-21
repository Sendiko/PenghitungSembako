package id.my.sendiko.sembako.grocery.list.data.dto

import com.google.gson.annotations.SerializedName

data class SaveTransactionRequest(

	@field:SerializedName("quantity")
	val quantity: String,

	@field:SerializedName("totalPrice")
	val totalPrice: Int,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("groceryId")
	val groceryId: Int
)
