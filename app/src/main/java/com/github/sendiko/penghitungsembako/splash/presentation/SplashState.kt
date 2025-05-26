package com.github.sendiko.penghitungsembako.splash.presentation

import com.github.sendiko.penghitungsembako.login.domain.User

data class SplashState(
    val user: User = User("", "", "")
)
