package com.github.sendiko.penghitungsembako.splash.presentation

import com.github.sendiko.penghitungsembako.core.domain.User

data class SplashState(
    val user: User = User("", "", "")
)
