package id.my.sendiko.sembako.store.list.presentation

sealed interface StoreEvent {
    data object OnClearState : StoreEvent
    data object OnLoadData : StoreEvent
}