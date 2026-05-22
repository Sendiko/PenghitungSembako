package id.my.sendiko.sembako.store.domain

import id.my.sendiko.sembako.user.core.domain.User
import kotlinx.coroutines.flow.Flow

interface StoreRepository {

    suspend fun getStores(userId: Int): Result<List<Store>>

    fun getUser(): Flow<User>

}