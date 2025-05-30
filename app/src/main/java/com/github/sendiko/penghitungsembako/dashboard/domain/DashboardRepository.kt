package com.github.sendiko.penghitungsembako.dashboard.domain

import com.github.sendiko.penghitungsembako.core.domain.User
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    fun getUser(): Flow<User>
}