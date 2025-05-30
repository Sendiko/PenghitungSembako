package com.github.sendiko.penghitungsembako.core.navigation

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
object ProfileDestination

@Serializable
object AboutDestination

@Serializable
data class FormDestination(
    val id: Int? = null
)