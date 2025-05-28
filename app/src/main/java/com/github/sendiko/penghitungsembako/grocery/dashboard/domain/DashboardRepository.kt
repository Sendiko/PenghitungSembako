package com.github.sendiko.penghitungsembako.grocery.dashboard.domain

import com.github.sendiko.penghitungsembako.core.domain.User
import com.github.sendiko.penghitungsembako.core.preferences.UiMode
import com.github.sendiko.penghitungsembako.grocery.core.domain.Grocery
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {

    fun getUser(): Flow<User>

    suspend fun getRemoteGroceries(userId: String): Result<List<Grocery>>

    suspend fun getLocalGroceries(): Flow<List<Grocery>>

    suspend fun setUiMode(uiMode: UiMode)

    fun getUiMode(): Flow<UiMode>

}