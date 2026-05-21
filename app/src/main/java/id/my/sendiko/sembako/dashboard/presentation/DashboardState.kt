package id.my.sendiko.sembako.dashboard.presentation

import id.my.sendiko.sembako.user.core.domain.User

data class DashboardState(
    val user: User = User(0, "", "", "", false),
    val message: String = "",
    val isError: Boolean = true,
    val isLoading: Boolean = false,
    val isSigningIn: Boolean = false,
    val isSignInSuccessful: Boolean = false,
    val signInError: String = "",
    val isStoreSheetVisible: Boolean = false,
    val storeName: String = "",
    val storeAddress: String = "",
    val storePhone: String = "",
    val storeEmail: String = ""
)
