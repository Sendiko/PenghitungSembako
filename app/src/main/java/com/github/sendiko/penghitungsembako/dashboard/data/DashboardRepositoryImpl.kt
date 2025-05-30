package com.github.sendiko.penghitungsembako.dashboard.data

import com.github.sendiko.penghitungsembako.core.domain.User
import com.github.sendiko.penghitungsembako.core.preferences.UserPreferences
import com.github.sendiko.penghitungsembako.dashboard.domain.DashboardRepository
import kotlinx.coroutines.flow.Flow

class DashboardRepositoryImpl(
    private val prefs: UserPreferences,
) : DashboardRepository {
    override fun getUser(): Flow<User> {
        return prefs.getUser()
    }
}