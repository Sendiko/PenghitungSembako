package id.my.sendiko.sembako.grocery.list.presentation

import id.my.sendiko.sembako.core.preferences.UiMode
import id.my.sendiko.sembako.grocery.core.domain.Grocery
import id.my.sendiko.sembako.store.core.domain.Store

sealed interface GroceryListEvent {
    data class OnGroceryChange(val sembako: Grocery) : GroceryListEvent
    data class OnQuantityChange(val quantity: String) : GroceryListEvent
    data class OnUnitChange(val unit: Boolean) : GroceryListEvent
    data object OnCalculateClick : GroceryListEvent
    data object OnDismiss : GroceryListEvent
    data class SetPreference(val uiMode: UiMode) : GroceryListEvent
    data object OnSaveTransaction: GroceryListEvent
    data object ClearState: GroceryListEvent
    data object LoadData: GroceryListEvent
    data class OnStoreChange(val store: Store) : GroceryListEvent
}