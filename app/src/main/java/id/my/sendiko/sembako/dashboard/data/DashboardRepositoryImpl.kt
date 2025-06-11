package id.my.sendiko.sembako.dashboard.data

import id.my.sendiko.sembako.core.domain.User
import id.my.sendiko.sembako.core.preferences.UserPreferences
import id.my.sendiko.sembako.dashboard.domain.DashboardRepository
import kotlinx.coroutines.flow.Flow

class DashboardRepositoryImpl(
    private val prefs: UserPreferences,
) : DashboardRepository {
    override fun getUser(): Flow<User> {
        return prefs.getUser()
    }
}