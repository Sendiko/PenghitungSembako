package com.github.sendiko.penghitungsembako.splash.data

import com.github.sendiko.penghitungsembako.core.preferences.UserPreferences
import com.github.sendiko.penghitungsembako.splash.domain.SplashRepository
import com.github.sendiko.penghitungsembako.user.domain.User
import kotlinx.coroutines.flow.Flow

class SplashRepositoryImpl(
    private val userPreferences: UserPreferences
): SplashRepository {

    override fun getUser(): Flow<User> {
        return userPreferences.getUser()
    }

}