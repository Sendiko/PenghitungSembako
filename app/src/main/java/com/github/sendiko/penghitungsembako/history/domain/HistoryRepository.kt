package com.github.sendiko.penghitungsembako.history.domain

import com.github.sendiko.penghitungsembako.core.domain.User
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    fun getUser(): Flow<User>

    suspend fun getHistory(id: String): Result<List<History>>

}