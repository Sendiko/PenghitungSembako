package com.github.sendiko.penghitungsembako.sembako.dashboard.presentation

import com.github.sendiko.penghitungsembako.sembako.core.data.Sembako
import com.github.sendiko.penghitungsembako.sembako.core.data.listSembako

data class DashboardState(
    val sembako: List<Sembako> = listSembako,
    val selectedSembako: Sembako? = null,
    val usingOns: Boolean = false,
    val quantity: String = "",
    val totalPrice: Double = 0.0,
    val message: String = "",
)
