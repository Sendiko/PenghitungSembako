package com.github.sendiko.penghitungsembako.grocery.list.domain

import com.github.sendiko.penghitungsembako.core.domain.User
import com.github.sendiko.penghitungsembako.core.preferences.UiMode
import com.github.sendiko.penghitungsembako.grocery.core.domain.Grocery
import com.github.sendiko.penghitungsembako.grocery.list.data.dto.SaveTransactionRequest
import kotlinx.coroutines.flow.Flow

interface ListRepository {
    fun getUser(): Flow<User>

    suspend fun getRemoteGroceries(userId: String): Result<List<Grocery>>

    suspend fun getLocalGroceries(): Flow<List<Grocery>>

    suspend fun saveGroceries(groceries: List<Grocery>): Result<Boolean>

    suspend fun setUiMode(uiMode: UiMode)

    fun getUiMode(): Flow<UiMode>

    suspend fun saveTransaction(request: SaveTransactionRequest): Result<Int>

}