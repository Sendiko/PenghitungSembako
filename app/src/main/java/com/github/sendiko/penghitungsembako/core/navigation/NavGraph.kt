package com.github.sendiko.penghitungsembako.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.github.sendiko.penghitungsembako.about.presentation.AboutScreenRoot
import com.github.sendiko.penghitungsembako.core.di.SembakoApplication
import com.github.sendiko.penghitungsembako.core.di.viewModelFactory
import com.github.sendiko.penghitungsembako.sembako.dashboard.presentation.DashboardScreen
import com.github.sendiko.penghitungsembako.sembako.dashboard.presentation.DashboardViewModel
import com.github.sendiko.penghitungsembako.sembako.form.presentation.FormScreen
import com.github.sendiko.penghitungsembako.sembako.form.presentation.FormViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
) {
    NavHost(
        startDestination = DashboardDestination,
        navController = navController,
        builder = {
            composable<DashboardDestination> {
                val viewModel = viewModel<DashboardViewModel>(
                    factory = viewModelFactory {
                        DashboardViewModel(
                            dao = SembakoApplication.module.sembakoDao,
                            prefs = SembakoApplication.module.userPreferences
                        )
                    }
                )
                val state by viewModel.state.collectAsStateWithLifecycle()

                DashboardScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onNavigate = {
                        navController.navigate(it)
                    }
                )
            }
            composable<AboutDestination> {
                AboutScreenRoot(navController)
            }
            composable<FormDestination> {
                val args = it.toRoute<FormDestination>()

                val viewModel = viewModel<FormViewModel>(
                    factory = viewModelFactory {
                        FormViewModel(SembakoApplication.module.sembakoDao)
                    }
                )
                LaunchedEffect(true) {
                    viewModel.setId(args.id)
                }
                val state by viewModel.state.collectAsStateWithLifecycle()

                FormScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onNavigateBack = { navController.navigateUp() }
                )
            }
        }
    )
}