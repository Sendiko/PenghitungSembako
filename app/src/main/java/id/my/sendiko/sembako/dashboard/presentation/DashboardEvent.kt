package id.my.sendiko.sembako.dashboard.presentation

import androidx.credentials.GetCredentialResponse

sealed interface DashboardEvent {
    data object OnLoginClicked: DashboardEvent
    data class OnResult(val result: Result<GetCredentialResponse>): DashboardEvent
    data object ClearState: DashboardEvent
    data class OnStoreSheetVisible(val isVisible: Boolean): DashboardEvent
    data class OnStoreNameChange(val name: String): DashboardEvent
    data class OnStoreAddressChange(val address: String): DashboardEvent
    data class OnStorePhoneChange(val phone: String): DashboardEvent
    data class OnStoreEmailChange(val email: String): DashboardEvent
    data object OnSaveStore: DashboardEvent
}
