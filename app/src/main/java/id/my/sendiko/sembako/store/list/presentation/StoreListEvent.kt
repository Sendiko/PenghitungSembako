package id.my.sendiko.sembako.store.list.presentation

import id.my.sendiko.sembako.store.core.domain.Store

sealed interface StoreListEvent {
    data object OnClearState : StoreListEvent
    data object OnLoadData : StoreListEvent
    data class OnStoreSheetVisible(val isVisible: Boolean) : StoreListEvent
    data class OnStoreNameChange(val name: String) : StoreListEvent
    data class OnStoreAddressChange(val address: String) : StoreListEvent
    data class OnStorePhoneChange(val phone: String) : StoreListEvent
    data class OnStoreEmailChange(val email: String) : StoreListEvent
    data object OnSaveStore : StoreListEvent
    data class OnEditStore(val store: Store) : StoreListEvent
    data object OnUpdateStore : StoreListEvent
    data object OnDeleteStore : StoreListEvent
    data class OnDeleteDialogOpen(val isOpen: Boolean) : StoreListEvent
}