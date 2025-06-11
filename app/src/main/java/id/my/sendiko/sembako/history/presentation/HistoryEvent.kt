package id.my.sendiko.sembako.history.presentation

sealed interface HistoryEvent {
    object LoadData: HistoryEvent
}