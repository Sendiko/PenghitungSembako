package id.my.sendiko.sembako.splash.presentation

import id.my.sendiko.sembako.core.domain.User

data class SplashState(
    val user: User = User(0, "", "", "")
)
