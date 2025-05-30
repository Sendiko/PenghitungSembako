package com.github.sendiko.penghitungsembako.grocery.list.presentation

import com.github.sendiko.penghitungsembako.core.preferences.UiMode
import com.github.sendiko.penghitungsembako.grocery.core.domain.Grocery

sealed interface ListEvent {
    data class OnSembakoClick(val sembako: Grocery) : ListEvent
    data class OnQuantityChange(val quantity: String) : ListEvent
    data class OnUnitChange(val unit: Boolean) : ListEvent
    data object OnCalculateClick : ListEvent
    data object OnDismiss : ListEvent
    data class SetPreference(val uiMode: UiMode) : ListEvent
    data object ClearState: ListEvent
    data object LoadData: ListEvent
}