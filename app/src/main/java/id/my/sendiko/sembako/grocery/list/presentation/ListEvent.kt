package id.my.sendiko.sembako.grocery.list.presentation

import id.my.sendiko.sembako.core.preferences.UiMode
import id.my.sendiko.sembako.grocery.core.domain.Grocery

sealed interface ListEvent {
    data class OnGroceryChange(val sembako: Grocery) : ListEvent
    data class OnQuantityChange(val quantity: String) : ListEvent
    data class OnUnitChange(val unit: Boolean) : ListEvent
    data object OnCalculateClick : ListEvent
    data object OnDismiss : ListEvent
    data class SetPreference(val uiMode: UiMode) : ListEvent
    data object OnSaveTransaction: ListEvent
    data object ClearState: ListEvent
    data object LoadData: ListEvent
}