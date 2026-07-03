package id.my.sendiko.sembako.statistics.presentation

import id.my.sendiko.sembako.user.core.domain.User
import id.my.sendiko.sembako.statistics.data.dto.StatisticsItem
import id.my.sendiko.sembako.statistics.domain.Statistics
import id.my.sendiko.sembako.store.core.domain.Store

data class StatisticsState(
    val isLoading: Boolean = false,
    val message: String = "",
    val statistics: Statistics? = null,
    val user: User? = null,
    val stores: List<Store> = emptyList(),
    val selectedStore: Store? = null,
)
