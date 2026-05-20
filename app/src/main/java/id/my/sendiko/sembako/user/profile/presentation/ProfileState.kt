package id.my.sendiko.sembako.user.profile.presentation

import id.my.sendiko.sembako.user.core.domain.User

data class ProfileState(
    val isSigningOut: Boolean = false,
    val isSignOutSuccessful: Boolean = false,
    val signOutError: String = "",
    val user: User? = null,
    val dynamicTheme: Boolean = true,
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)
