package com.github.sendiko.penghitungsembako.login.domain

import com.github.sendiko.penghitungsembako.core.domain.User

interface LoginRepository {

    suspend fun saveUserToRemote(user: User): Result<User>

    suspend fun saveUserToLocal(user: User): Result<Boolean>

}