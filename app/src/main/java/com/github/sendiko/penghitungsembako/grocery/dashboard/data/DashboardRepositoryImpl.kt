package com.github.sendiko.penghitungsembako.grocery.dashboard.data

import com.github.sendiko.penghitungsembako.core.domain.User
import com.github.sendiko.penghitungsembako.core.preferences.UiMode
import com.github.sendiko.penghitungsembako.core.preferences.UserPreferences
import com.github.sendiko.penghitungsembako.grocery.core.data.Sembako
import com.github.sendiko.penghitungsembako.grocery.core.data.SembakoDao
import com.github.sendiko.penghitungsembako.grocery.dashboard.domain.DashboardRepository
import kotlinx.coroutines.flow.Flow

class DashboardRepositoryImpl(
    private val prefs: UserPreferences,
    private val dao: SembakoDao
) : DashboardRepository {

    override fun getUser(): Flow<User> {
        return prefs.getUser()
    }

    override fun getAllGroceries(): Flow<List<Sembako>> {
        return dao.getAll()
    }

    override suspend fun setUiMode(uiMode: UiMode) {
        prefs.setUiMode(uiMode)
    }

    override fun getUiMode(): Flow<UiMode> {
        return prefs.getUiMode()
    }


}