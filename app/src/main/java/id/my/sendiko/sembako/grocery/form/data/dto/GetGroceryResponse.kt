package id.my.sendiko.sembako.grocery.form.data.dto

import com.google.gson.annotations.SerializedName

data class GetGroceryResponse(

	@field:SerializedName("grocery")
	val groceryItem: GroceryItem,

	@field:SerializedName("status")
	val status: Int
)