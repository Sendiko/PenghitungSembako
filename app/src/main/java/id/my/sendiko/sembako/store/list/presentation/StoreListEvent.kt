package id.my.sendiko.sembako.store.list.presentation

sealed interface StoreListEvent {
    data object OnClearState : StoreListEvent
    data object OnLoadData : StoreListEvent
}