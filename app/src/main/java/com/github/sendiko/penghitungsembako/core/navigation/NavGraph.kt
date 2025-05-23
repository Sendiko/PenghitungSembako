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
import com.github.sendiko.penghitungsembako.splash.data.SplashRepositoryImpl
import com.github.sendiko.penghitungsembako.splash.presentation.SplashScreen
import com.github.sendiko.penghitungsembako.splash.presentation.SplashViewModel
import com.github.sendiko.penghitungsembako.user.presentation.LoginScreen
import com.github.sendiko.penghitungsembako.user.presentation.LoginViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
) {
    NavHost(
        startDestination = SplashDestination,
        navController = navController,
        builder = {
            composable<SplashDestination> {
                val viewModel = viewModel<SplashViewModel>(
                    factory = viewModelFactory {
                        val splashRepository =
                            SplashRepositoryImpl(SembakoApplication.module.userPreferences)
                        SplashViewModel(splashRepository)
                    }
                )

                val state by viewModel.state.collectAsStateWithLifecycle()

                SplashScreen(
                    state = state,
                    onNavigate = {
                        navController.navigate(it) {
                            popUpTo(SplashDestination)
                        }
                    }
                )
            }
            composable<LoginDestination> {
                val viewModel = viewModel<LoginViewModel>(
                    factory = viewModelFactory {
                        LoginViewModel(SembakoApplication.module.application)
                    }
                )
                val state by viewModel.state.collectAsStateWithLifecycle()

                LoginScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onNavigate = {
                        navController.navigate(DashboardDestination) {
                            popUpTo(DashboardDestination) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
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