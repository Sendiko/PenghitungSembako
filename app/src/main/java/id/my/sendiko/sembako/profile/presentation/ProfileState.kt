package id.my.sendiko.sembako.profile.presentation

import id.my.sendiko.sembako.core.domain.User
import id.my.sendiko.sembako.statistics.data.dto.Statistics

data class ProfileState(
    val isSigningOut: Boolean = false,
    val isSignOutSuccessful: Boolean = false,
    val signOutError: String = "",
    val user: User? = null,
    val dynamicTheme: Boolean = true,
    val isLoading: Boolean = false,
    val statistics: Statistics? = null,
    val errorMessage: String = ""
)
