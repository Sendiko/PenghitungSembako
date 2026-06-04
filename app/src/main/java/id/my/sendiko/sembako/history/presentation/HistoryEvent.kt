package id.my.sendiko.sembako.history.presentation

import id.my.sendiko.sembako.store.core.domain.Store

sealed interface HistoryEvent {
    object LoadData: HistoryEvent
    object ClearState: HistoryEvent
    data class OnStoreChange(val store: Store): HistoryEvent
}