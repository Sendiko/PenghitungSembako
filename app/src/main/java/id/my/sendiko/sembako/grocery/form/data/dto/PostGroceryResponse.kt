package id.my.sendiko.sembako.grocery.form.data.dto

import com.google.gson.annotations.SerializedName

data class PostGroceryResponse(

    @field:SerializedName("grocery")
	val groceryItem: GroceryItem? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Int? = null
)
