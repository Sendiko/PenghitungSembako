package id.my.sendiko.sembako.dashboard.presentation

import id.my.sendiko.sembako.core.domain.User

data class DashboardState(
    val user: User = User(0, "", "", ""),
    val message: String = "",
    val isLoading: Boolean = false,
    val isSigningIn: Boolean = false,
    val isSignInSuccessful: Boolean = false,
    val signInError: String = ""
)
