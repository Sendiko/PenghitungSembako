package id.my.sendiko.sembako.store.core.data.dto

import com.google.gson.annotations.SerializedName

data class GetStoresResponse(
    @field:SerializedName("store")
    val stores: List<StoreDto>,

    @field:SerializedName("status")
    val status: Int
)
