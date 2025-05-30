package com.github.sendiko.penghitungsembako.grocery.dashboard.data

import com.github.sendiko.penghitungsembako.core.domain.User
import com.github.sendiko.penghitungsembako.core.network.ApiService
import com.github.sendiko.penghitungsembako.core.preferences.UserPreferences
import com.github.sendiko.penghitungsembako.grocery.core.data.GroceryDao
import com.github.sendiko.penghitungsembako.grocery.dashboard.domain.DashboardRepository
import kotlinx.coroutines.flow.Flow

class DashboardRepositoryImpl(
    private val prefs: UserPreferences,
    private val localDataSource: GroceryDao,
    private val remoteDataSource: ApiService
) : DashboardRepository {
    override fun getUser(): Flow<User> {
        return prefs.getUser()
    }
}