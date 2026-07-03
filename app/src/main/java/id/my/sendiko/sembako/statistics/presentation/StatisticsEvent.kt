package id.my.sendiko.sembako.statistics.presentation

import id.my.sendiko.sembako.store.core.domain.Store

sealed interface StatisticsEvent {
    data object LoadData : StatisticsEvent
    data object ClearState : StatisticsEvent
    data class OnStoreChange(val store: Store) : StatisticsEvent
}