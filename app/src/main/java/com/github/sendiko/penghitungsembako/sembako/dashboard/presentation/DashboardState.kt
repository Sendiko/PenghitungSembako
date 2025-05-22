package com.github.sendiko.penghitungsembako.sembako.dashboard.presentation

import com.github.sendiko.penghitungsembako.core.preferences.UiMode
import com.github.sendiko.penghitungsembako.sembako.core.data.Sembako

data class DashboardState(
    val sembako: List<Sembako> = emptyList(),
    val selectedSembako: Sembako? = null,
    val usingOns: Boolean = false,
    val quantity: String = "",
    val totalPrice: Double = 0.0,
    val message: String = "",
    val uiMode: UiMode = UiMode.GRID
)
