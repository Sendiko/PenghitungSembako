package com.github.sendiko.penghitungsembako.grocery.dashboard.presentation

import com.github.sendiko.penghitungsembako.core.preferences.UiMode
import com.github.sendiko.penghitungsembako.grocery.core.data.Sembako

sealed interface DashboardEvent {
    data class OnSembakoClick(val sembako: Sembako) : DashboardEvent
    data class OnQuantityChange(val quantity: String) : DashboardEvent
    data class OnUnitChange(val unit: Boolean) : DashboardEvent
    data object OnCalculateClick : DashboardEvent
    data object OnDismiss : DashboardEvent
    data class SetPreference(val uiMode: UiMode) : DashboardEvent
    data object ClearState: DashboardEvent
    data object LoadData: DashboardEvent
}