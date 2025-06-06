package id.my.sendiko.sembako.splash.data

import id.my.sendiko.sembako.core.preferences.UserPreferences
import id.my.sendiko.sembako.splash.domain.SplashRepository
import id.my.sendiko.sembako.core.domain.User
import kotlinx.coroutines.flow.Flow

class SplashRepositoryImpl(
    private val userPreferences: UserPreferences
): SplashRepository {

    override fun getUser(): Flow<User> {
        return userPreferences.getUser()
    }

}