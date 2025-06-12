package id.my.sendiko.sembako.statistics.presentation

sealed interface StatisticsEvent {
    data object LoadData: StatisticsEvent
    data object ClearState: StatisticsEvent
}