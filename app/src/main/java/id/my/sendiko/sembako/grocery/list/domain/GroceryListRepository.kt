package id.my.sendiko.sembako.grocery.list.domain

import id.my.sendiko.sembako.user.core.domain.User
import id.my.sendiko.sembako.core.preferences.UiMode
import id.my.sendiko.sembako.grocery.core.domain.Grocery
import id.my.sendiko.sembako.grocery.list.data.dto.SaveTransactionRequest
import id.my.sendiko.sembako.store.core.domain.Store
import kotlinx.coroutines.flow.Flow

interface GroceryListRepository {
    fun getUser(): Flow<User>

    suspend fun getRemoteGroceries(userId: String): Result<List<Grocery>>

    suspend fun getLocalGroceries(): Flow<List<Grocery>>

    suspend fun saveGroceries(groceries: List<Grocery>): Result<Boolean>

    suspend fun setUiMode(uiMode: UiMode)

    fun getUiMode(): Flow<UiMode>

    suspend fun saveTransaction(request: SaveTransactionRequest): Result<Int>

    suspend fun getStores(userId: Int): Result<List<Store>>

}