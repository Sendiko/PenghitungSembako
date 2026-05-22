package id.my.sendiko.sembako.store.presentation

sealed interface StoreEvent {
    data object OnClearState : StoreEvent
    data object OnLoadData : StoreEvent
}