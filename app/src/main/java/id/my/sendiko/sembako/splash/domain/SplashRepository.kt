package id.my.sendiko.sembako.splash.domain

import id.my.sendiko.sembako.core.domain.User
import kotlinx.coroutines.flow.Flow

interface SplashRepository {

    fun getUser(): Flow<User>

}