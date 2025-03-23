package com.github.sendiko.penghitungsembako.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.sendiko.penghitungsembako.about.presentation.AboutScreenRoot
import com.github.sendiko.penghitungsembako.dashboard.presentation.DashboardScreenRoot

@Composable
fun NavGraph(
    navController: NavHostController,
) {
    NavHost(
        startDestination = DashboardDestination,
        navController = navController,
        builder = {
            composable<DashboardDestination> {
                DashboardScreenRoot(navController)
            }
            composable<AboutDestination> {
                AboutScreenRoot(navController)
            }
        }
    )
}