package com.github.sendiko.penghitungsembako.user.presentation

data class LoginState(
    val isSigningIn: Boolean = false,
    val isSignInSuccessful: Boolean = false,
    val signInError: String = ""
)
