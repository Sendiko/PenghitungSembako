package id.my.sendiko.sembako.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object SplashDestination

@Serializable
object LoginDestination

@Serializable
object DashboardDestination

@Serializable
object ListDestination

@Serializable
object StatisticsDestination

@Serializable
object HistoryDestination

@Serializable
object ProfileDestination

@Serializable
object AboutDestination

@Serializable
data class FormDestination(
    val id: Int? = null
)