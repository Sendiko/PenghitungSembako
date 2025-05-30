package com.github.sendiko.penghitungsembako.statistics.data.dto

import com.google.gson.annotations.SerializedName

data class GetStatisticsResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int,

	@field:SerializedName("statistics")
	val statistics: Statistics
)

data class Statistics(

	@field:SerializedName("totalHistory")
	val totalHistory: Int,

	@field:SerializedName("groceryCount")
	val groceryCount: Int,

	@field:SerializedName("totalSales")
	val totalSales: Int
)
