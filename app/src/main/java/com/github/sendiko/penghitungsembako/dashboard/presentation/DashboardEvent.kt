package com.github.sendiko.penghitungsembako.dashboard.presentation

import com.github.sendiko.penghitungsembako.core.data.Sembako

sealed interface DashboardEvent {
    data class OnSembakoClick(val sembako: Sembako) : DashboardEvent
    data class OnQuantityChange(val quantity: String) : DashboardEvent
    data class OnUnitChange(val unit: Boolean) : DashboardEvent
    data object OnCalculateClick : DashboardEvent
    data object OnDismiss : DashboardEvent
}