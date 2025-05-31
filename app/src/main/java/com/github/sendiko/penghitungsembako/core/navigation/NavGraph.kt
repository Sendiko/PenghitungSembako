package com.github.sendiko.penghitungsembako.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.github.sendiko.penghitungsembako.about.presentation.AboutScreenRoot
import com.github.sendiko.penghitungsembako.core.di.SembakoApplication
import com.github.sendiko.penghitungsembako.core.di.viewModelFactory
import com.github.sendiko.penghitungsembako.core.navigation.StatisticsDestination
import com.github.sendiko.penghitungsembako.core.preferences.UserPreferences
import com.github.sendiko.penghitungsembako.core.preferences.dataStore
import com.github.sendiko.penghitungsembako.login.data.LoginRepositoryImpl
import com.github.sendiko.penghitungsembako.profile.data.ProfileRepositoryImpl
import com.github.sendiko.penghitungsembako.profile.presentation.ProfileScreen
import com.github.sendiko.penghitungsembako.profile.presentation.ProfileViewModel
import com.github.sendiko.penghitungsembako.dashboard.presentation.DashboardScreen
import com.github.sendiko.penghitungsembako.dashboard.presentation.DashboardViewModel
import com.github.sendiko.penghitungsembako.grocery.form.presentation.FormScreen
import com.github.sendiko.penghitungsembako.grocery.form.presentation.FormViewModel
import com.github.sendiko.penghitungsembako.splash.data.SplashRepositoryImpl
import com.github.sendiko.penghitungsembako.splash.presentation.SplashScreen
import com.github.sendiko.penghitungsembako.splash.presentation.SplashViewModel
import com.github.sendiko.penghitungsembako.login.presentation.LoginScreen
import com.github.sendiko.penghitungsembako.login.presentation.LoginViewModel
import com.github.sendiko.penghitungsembako.dashboard.data.DashboardRepositoryImpl
import com.github.sendiko.penghitungsembako.grocery.form.data.FormRepositoryImpl
import com.github.sendiko.penghitungsembako.grocery.list.data.ListRepositoryImpl
import com.github.sendiko.penghitungsembako.grocery.list.presentation.ListScreen
import com.github.sendiko.penghitungsembako.grocery.list.presentation.ListViewModel
import com.github.sendiko.penghitungsembako.history.data.HistoryRepositoryImpl
import com.github.sendiko.penghitungsembako.history.presentation.HistoryScreen
import com.github.sendiko.penghitungsembako.history.presentation.HistoryViewModel
import com.github.sendiko.penghitungsembako.statistics.data.StatisticsRepositoryImpl
import com.github.sendiko.penghitungsembako.statistics.presentation.StatisticsScreen
import com.github.sendiko.penghitungsembako.statistics.presentation.StatisticsViewModel

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
                        val loginRepository = LoginRepositoryImpl(
                            remoteDataSource = SembakoApplication.module.apiService,
                            localDataSource = SembakoApplication.module.userPreferences
                        )
                        LoginViewModel(loginRepository)
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
                        val repository = DashboardRepositoryImpl(
                            prefs = SembakoApplication.module.userPreferences
                        )
                        DashboardViewModel(repository)
                    }
                )
                val state by viewModel.state.collectAsStateWithLifecycle()

                DashboardScreen(
                    state = state,
                    onNavigate = {
                        navController.navigate(it)
                    }
                )
            }
            composable<ListDestination> {
                val viewModel = viewModel<ListViewModel>(
                    factory = viewModelFactory {
                        val repository = ListRepositoryImpl(
                            remoteDataSource = SembakoApplication.module.apiService,
                            localDataSource = SembakoApplication.module.sembakoDao,
                            prefs = SembakoApplication.module.userPreferences
                        )
                        ListViewModel(repository)
                    }
                )
                val state by viewModel.state.collectAsStateWithLifecycle()

                ListScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onNavigate = {
                        if (it == null) {
                            navController.navigateUp()
                        } else {
                            navController.navigate(it)
                        }
                    }
                )
            }
            composable<ProfileDestination> {
                val context = LocalContext.current
                val viewModel = viewModel<ProfileViewModel>(
                    factory = viewModelFactory {
                        val profileRepository = ProfileRepositoryImpl(
                            context = context,
                            userPreferences = UserPreferences(SembakoApplication.module.application.dataStore),
                        )
                        ProfileViewModel(profileRepository)
                    }
                )

                val state by viewModel.state.collectAsStateWithLifecycle()

                ProfileScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onNavigate = {
                        when(it) {
                            null -> navController.navigateUp()
                            is SplashDestination -> navController.navigate(SplashDestination) {
                                popUpTo(SplashDestination) { inclusive = true }
                            }
                            else -> navController.navigate(it)
                        }
                    }
                )
            }
            composable<StatisticsDestination> {
                val viewModel = viewModel<StatisticsViewModel>(
                    factory = viewModelFactory {
                        val repository = StatisticsRepositoryImpl(
                            remoteDataSource = SembakoApplication.module.apiService,
                            userPreferences = SembakoApplication.module.userPreferences
                        )
                        StatisticsViewModel(repository)
                    }
                )
                val state by viewModel.state.collectAsStateWithLifecycle()

                StatisticsScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onNavigateUp = { navController.navigateUp() }
                )
            }
            composable<HistoryDestination> {
                val viewModel = viewModel<HistoryViewModel>(
                    factory = viewModelFactory {
                        val repository = HistoryRepositoryImpl(
                            userPreferences = SembakoApplication.module.userPreferences,
                            remoteDataSource = SembakoApplication.module.apiService
                        )
                        HistoryViewModel(repository)
                    }
                )
                val state by viewModel.state.collectAsStateWithLifecycle()

                HistoryScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onNavigateUp = { navController.navigateUp() }
                )
            }
            composable<AboutDestination> {
                AboutScreenRoot(navController)
            }
            composable<FormDestination> {
                val args = it.toRoute<FormDestination>()

                val viewModel = viewModel<FormViewModel>(
                    factory = viewModelFactory {
                        val repository = FormRepositoryImpl(
                            localDataSource = SembakoApplication.module.sembakoDao,
                            remoteDataSource = SembakoApplication.module.apiService,
                            userPreferences = SembakoApplication.module.userPreferences
                        )
                        FormViewModel(repository)
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