package com.github.sendiko.penghitungsembako.profile.presentation

import com.github.sendiko.penghitungsembako.core.domain.User

data class ProfileState(
    val isSigningOut: Boolean = false,
    val isSignOutSuccessful: Boolean = false,
    val signOutError: String = "",
    val user: User? = null,
    val dynamicTheme: Boolean = true,
)
