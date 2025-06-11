package id.my.sendiko.sembako.grocery.list.data.dto

import com.google.gson.annotations.SerializedName

data class SaveTransactionRequest(

	@field:SerializedName("quantity")
	val quantity: Any,

	@field:SerializedName("totalPrice")
	val totalPrice: Int,

	@field:SerializedName("userId")
	val userId: Int,

	@field:SerializedName("groceryId")
	val groceryId: Int
)
