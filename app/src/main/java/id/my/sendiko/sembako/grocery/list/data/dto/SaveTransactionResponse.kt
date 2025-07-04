package id.my.sendiko.sembako.grocery.list.data.dto

import com.google.gson.annotations.SerializedName

data class SaveTransactionResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)
