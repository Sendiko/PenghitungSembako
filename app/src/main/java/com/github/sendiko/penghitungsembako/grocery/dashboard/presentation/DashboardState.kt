package com.github.sendiko.penghitungsembako.grocery.dashboard.presentation

import com.github.sendiko.penghitungsembako.core.domain.User
import com.github.sendiko.penghitungsembako.core.preferences.UiMode
import com.github.sendiko.penghitungsembako.grocery.core.data.GroceryEntity
import com.github.sendiko.penghitungsembako.grocery.core.domain.Grocery

data class DashboardState(
    val sembako: List<Grocery> = emptyList(),
    val selectedSembako: Grocery? = null,
    val usingOns: Boolean = false,
    val quantity: String = "",
    val totalPrice: Double = 0.0,
    val message: String = "",
    val uiMode: UiMode = UiMode.GRID,
    val user: User? = null,
    val isLoading: Boolean = false,
)
