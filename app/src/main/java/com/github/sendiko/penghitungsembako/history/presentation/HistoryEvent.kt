package com.github.sendiko.penghitungsembako.history.presentation

sealed interface HistoryEvent {
    object LoadData: HistoryEvent
}