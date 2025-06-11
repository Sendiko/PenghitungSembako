package id.my.sendiko.sembako.dashboard.domain

import id.my.sendiko.sembako.core.domain.User
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    fun getUser(): Flow<User>
}