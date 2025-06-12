package id.my.sendiko.sembako.statistics.domain

import id.my.sendiko.sembako.statistics.data.dto.StatisticsItem

data class Statistics(
    val totalHistory: Int,
    val groceryCount: Int,
    val totalSales: Int
) {
    companion object {
        fun fromStatisticsItem(statisticsItem: StatisticsItem): Statistics {
            return Statistics(
                totalHistory = statisticsItem.totalHistory,
                groceryCount = statisticsItem.groceryCount,
                totalSales = statisticsItem.totalSales
            )
        }
    }
}
