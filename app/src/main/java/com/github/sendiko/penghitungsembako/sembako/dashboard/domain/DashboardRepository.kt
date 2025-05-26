package com.github.sendiko.penghitungsembako.sembako.dashboard.domain

import com.github.sendiko.penghitungsembako.core.domain.User
import com.github.sendiko.penghitungsembako.core.preferences.UiMode
import com.github.sendiko.penghitungsembako.sembako.core.data.Sembako
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {

    fun getUser(): Flow<User>

    fun getAllGroceries(): Flow<List<Sembako>>

    suspend fun setUiMode(uiMode: UiMode)

    fun getUiMode(): Flow<UiMode>

}