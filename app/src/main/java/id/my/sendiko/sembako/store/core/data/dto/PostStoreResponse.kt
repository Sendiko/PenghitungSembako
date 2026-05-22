package id.my.sendiko.sembako.store.core.data.dto

import com.google.gson.annotations.SerializedName

data class PostStoreResponse(

	@field:SerializedName("store")
	val storeDto: StoreDto,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)
