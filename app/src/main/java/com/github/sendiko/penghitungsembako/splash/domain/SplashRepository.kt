package com.github.sendiko.penghitungsembako.splash.domain

import com.github.sendiko.penghitungsembako.core.domain.User
import kotlinx.coroutines.flow.Flow

interface SplashRepository {

    fun getUser(): Flow<User>

}