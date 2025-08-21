package id.my.sendiko.sembako.dashboard.presentation

sealed interface DashboardEvent {
    data object OnLoginClicked: DashboardEvent
    data object ClearState: DashboardEvent
}