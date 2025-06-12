package id.my.sendiko.sembako.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import id.my.sendiko.sembako.about.presentation.AboutScreenRoot
import id.my.sendiko.sembako.core.di.SembakoApplication
import id.my.sendiko.sembako.core.di.viewModelFactory
import id.my.sendiko.sembako.core.preferences.UserPreferences
import id.my.sendiko.sembako.core.preferences.dataStore
import id.my.sendiko.sembako.dashboard.data.DashboardRepositoryImpl
import id.my.sendiko.sembako.dashboard.presentation.DashboardScreen
import id.my.sendiko.sembako.dashboard.presentation.DashboardViewModel
import id.my.sendiko.sembako.grocery.form.data.FormRepositoryImpl
import id.my.sendiko.sembako.grocery.form.presentation.FormScreen
import id.my.sendiko.sembako.grocery.form.presentation.FormViewModel
import id.my.sendiko.sembako.grocery.list.data.ListRepositoryImpl
import id.my.sendiko.sembako.grocery.list.presentation.ListScreen
import id.my.sendiko.sembako.grocery.list.presentation.ListViewModel
import id.my.sendiko.sembako.history.data.HistoryRepositoryImpl
import id.my.sendiko.sembako.history.presentation.HistoryScreen
import id.my.sendiko.sembako.history.presentation.HistoryViewModel
import id.my.sendiko.sembako.profile.data.ProfileRepositoryImpl
import id.my.sendiko.sembako.profile.presentation.ProfileScreen
import id.my.sendiko.sembako.profile.presentation.ProfileViewModel
import id.my.sendiko.sembako.statistics.data.StatisticsRepositoryImpl
import id.my.sendiko.sembako.statistics.presentation.StatisticsScreen
import id.my.sendiko.sembako.statistics.presentation.StatisticsViewModel

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
                        val repository = DashboardRepositoryImpl(
                            remoteDataSource = SembakoApplication.module.apiService,
                            localDataSource = SembakoApplication.module.userPreferences
                        )
                        DashboardViewModel(repository)
                    }
                )
                val state by viewModel.state.collectAsStateWithLifecycle()

                DashboardScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    signInEventFlow = viewModel.signInEvent,
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
                            is DashboardDestination -> navController.navigate(DashboardDestination) {
                                popUpTo(DashboardDestination) { inclusive = true }
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
                            userPreferences = SembakoApplication.module.userPreferences,
                            localDataSource = SembakoApplication.module.statisticsPreferences
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
                            remoteDataSource = SembakoApplication.module.apiService,
                            localDataSource = SembakoApplication.module.historyDao
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