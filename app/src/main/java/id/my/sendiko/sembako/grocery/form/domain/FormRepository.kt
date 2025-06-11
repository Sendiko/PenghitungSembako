package id.my.sendiko.sembako.grocery.form.domain

import id.my.sendiko.sembako.core.domain.User
import id.my.sendiko.sembako.grocery.core.data.GroceryEntity
import id.my.sendiko.sembako.grocery.core.domain.Grocery
import id.my.sendiko.sembako.grocery.form.data.dto.PostGroceryRequest
import id.my.sendiko.sembako.grocery.form.data.dto.UpdateGroceryRequest
import kotlinx.coroutines.flow.Flow

interface FormRepository {

    suspend fun saveGroceryToRemote(request: PostGroceryRequest): Result<Int>

    suspend fun getGroceryFromRemote(id: Int): Result<Grocery>

    suspend fun updateGroceryToRemote(id: Int, request: UpdateGroceryRequest): Result<Int>

    suspend fun deleteGroceryFromRemote(id: Int): Result<Int>

    suspend fun saveGroceryToLocal(grocery: GroceryEntity)

    fun getUser(): Flow<User>

}