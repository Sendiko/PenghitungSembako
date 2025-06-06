package id.my.sendiko.sembako.login.domain

import id.my.sendiko.sembako.core.domain.User

interface LoginRepository {

    suspend fun saveUserToRemote(user: User): Result<User>

    suspend fun saveUserToLocal(user: User): Result<Boolean>

}