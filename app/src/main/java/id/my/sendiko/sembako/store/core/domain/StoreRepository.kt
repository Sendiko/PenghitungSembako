package id.my.sendiko.sembako.store.core.domain

import id.my.sendiko.sembako.store.core.domain.Store
import id.my.sendiko.sembako.user.core.domain.User
import kotlinx.coroutines.flow.Flow

interface StoreRepository {

    suspend fun getStores(userId: Int): Result<List<Store>>

    fun getUser(): Flow<User>

    suspend fun saveStore(store: Store): Result<Store>

    suspend fun updateStore(id: Int, store: Store): Result<Store>

    suspend fun deleteStore(id: Int): Result<Boolean>

}