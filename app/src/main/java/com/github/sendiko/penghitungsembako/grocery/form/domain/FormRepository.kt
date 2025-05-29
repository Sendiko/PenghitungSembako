package com.github.sendiko.penghitungsembako.grocery.form.domain

import com.github.sendiko.penghitungsembako.core.domain.User
import com.github.sendiko.penghitungsembako.grocery.core.data.GroceryEntity
import com.github.sendiko.penghitungsembako.grocery.core.domain.Grocery
import com.github.sendiko.penghitungsembako.grocery.form.data.dto.PostGroceryRequest
import com.github.sendiko.penghitungsembako.grocery.form.data.dto.UpdateGroceryRequest
import kotlinx.coroutines.flow.Flow

interface FormRepository {

    suspend fun saveGroceryToRemote(request: PostGroceryRequest): Result<Int>

    suspend fun getGroceryFromRemote(id: Int): Result<Grocery>

    suspend fun updateGroceryToRemote(id: Int, request: UpdateGroceryRequest): Result<Int>

    suspend fun deleteGroceryFromRemote(id: Int): Result<Int>

    suspend fun saveGroceryToLocal(grocery: GroceryEntity)

    fun getUser(): Flow<User>

}