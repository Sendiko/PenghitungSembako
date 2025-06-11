package id.my.sendiko.sembako.dashboard.presentation

import id.my.sendiko.sembako.core.domain.User

data class DashboardState(
    val user: User? = null,
    val message: String = "",
    val isLoading: Boolean = false,
)
