package com.github.sendiko.penghitungsembako.statistics.presentation

import com.github.sendiko.penghitungsembako.core.domain.User
import com.github.sendiko.penghitungsembako.statistics.data.dto.Statistics

data class StatisticsState(
    val isLoading: Boolean = false,
    val message: String = "",
    val statistics: Statistics? = null,
    val user: User? = null,
)
