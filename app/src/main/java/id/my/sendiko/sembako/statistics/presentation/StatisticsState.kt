package id.my.sendiko.sembako.statistics.presentation

import id.my.sendiko.sembako.core.domain.User
import id.my.sendiko.sembako.statistics.data.dto.Statistics

data class StatisticsState(
    val isLoading: Boolean = false,
    val message: String = "",
    val statistics: Statistics? = null,
    val user: User? = null,
)
