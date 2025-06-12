package id.my.sendiko.sembako.dashboard.presentation

import androidx.credentials.GetCredentialResponse

sealed interface DashboardEvent {
    data object OnLoginClicked: DashboardEvent
    data class OnResult(val result: GetCredentialResponse): DashboardEvent
    data object ClearState: DashboardEvent
}