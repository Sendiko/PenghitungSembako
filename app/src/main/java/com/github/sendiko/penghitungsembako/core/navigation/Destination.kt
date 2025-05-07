package com.github.sendiko.penghitungsembako.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object DashboardDestination

@Serializable
object AboutDestination

@Serializable
data class FormDestination(
    val id: Int? = null
)