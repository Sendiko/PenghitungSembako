package com.github.sendiko.penghitungsembako.dashboard.presentation

import com.github.sendiko.penghitungsembako.core.data.Sembako
import com.github.sendiko.penghitungsembako.dashboard.data.listSembako

data class DashboardState(
    val sembako: List<Sembako> = listSembako,
    var selectedSembako: Sembako? = null,
    var quantity: String = "",
    var totalPrice: Double = 0.0,
    val message: String = "",
)
