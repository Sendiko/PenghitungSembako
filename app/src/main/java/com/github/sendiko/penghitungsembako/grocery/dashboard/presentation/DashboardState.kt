package com.github.sendiko.penghitungsembako.grocery.dashboard.presentation

import com.github.sendiko.penghitungsembako.core.domain.User

data class DashboardState(
    val user: User? = null,
    val message: String = "",
    val isLoading: Boolean = false,
)
