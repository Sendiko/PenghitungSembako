package id.my.sendiko.sembako.core.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import id.my.sendiko.sembako.about.presentation.AboutScreenRoot
import id.my.sendiko.sembako.core.di.SembakoApplication
import id.my.sendiko.sembako.core.preferences.UserPreferences
import id.my.sendiko.sembako.core.preferences.dataStore
import id.my.sendiko.sembako.dashboard.data.DashboardRepositoryImpl
import id.my.sendiko.sembako.dashboard.presentation.DashboardScreen
import id.my.sendiko.sembako.dashboard.presentation.DashboardViewModel
import id.my.sendiko.sembako.grocery.form.data.GroceryFormRepositoryImpl
import id.my.sendiko.sembako.grocery.form.presentation.GroceryFormScreen
import id.my.sendiko.sembako.grocery.form.presentation.GroceryFormViewModel
import id.my.sendiko.sembako.grocery.list.data.GroceryListRepositoryImpl
import id.my.sendiko.sembako.grocery.list.presentation.GroceryListScreen
import id.my.sendiko.sembako.grocery.list.presentation.GroceryListViewModel
import id.my.sendiko.sembako.history.data.HistoryRepositoryImpl
import id.my.sendiko.sembako.history.presentation.HistoryScreen
import id.my.sendiko.sembako.history.presentation.HistoryViewModel
import id.my.sendiko.sembako.statistics.data.StatisticsRepositoryImpl
import id.my.sendiko.sembako.statistics.presentation.StatisticsScreen
import id.my.sendiko.sembako.statistics.presentation.StatisticsViewModel
import id.my.sendiko.sembako.store.core.data.StoreRepositoryImpl
import id.my.sendiko.sembako.store.list.presentation.StoreListScreen
import id.my.sendiko.sembako.store.list.presentation.StoreListViewModel
import id.my.sendiko.sembako.user.profile.data.ProfileRepositoryImpl
import id.my.sendiko.sembako.user.profile.presentation.ProfileScreen
import id.my.sendiko.sembako.user.profile.presentation.ProfileViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavGraph(
    navController: NavHostController,
) {
    SharedTransitionLayout {
        NavHost(
            startDestination = DashboardDestination,
            navController = navController,
            builder = {
                composable<DashboardDestination> {
                    val viewModel = viewModel<DashboardViewModel>(
                        factory = viewModelFactory {
                            initializer {
                                val repository = DashboardRepositoryImpl(
                                    userRemoteDataSource = SembakoApplication.module.userRemoteDataSource,
                                    userLocalDataSource = SembakoApplication.module.userPreferences,
                                    storeRemoteDataSource = SembakoApplication.module.storeDataSource
                                )
                                DashboardViewModel(repository)
                            }
                        }
                    )
                    val state by viewModel.state.collectAsStateWithLifecycle()

                    DashboardScreen(
                        state = state,
                        onEvent = viewModel::onEvent,
                        signInEventFlow = viewModel.signInEvent,
                        onNavigate = {
                            navController.navigate(it)
                        },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedContentScope = this@composable
                    )
                }
                composable<ListDestination> {
                    val viewModel = viewModel<GroceryListViewModel>(
                        factory = viewModelFactory {
                            initializer {
                                val repository = GroceryListRepositoryImpl(
                                    groceryRemoteDataSource = SembakoApplication.module.groceryDataSource,
                                    groceryLocalDataSource = SembakoApplication.module.sembakoDao,
                                    userLocalDataSource = SembakoApplication.module.userPreferences,
                                    storeRemoteDataSource = SembakoApplication.module.storeDataSource
                                )
                                GroceryListViewModel(repository)
                            }
                        }
                    )
                    val state by viewModel.state.collectAsStateWithLifecycle()

                    GroceryListScreen(
                        state = state,
                        onEvent = viewModel::onEvent,
                        onNavigate = {
                            if (it == null) {
                                navController.navigateUp()
                            } else {
                                navController.navigate(it)
                            }
                        },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedContentScope = this@composable
                    )
                }
                composable<ProfileDestination> {
                    val context = LocalContext.current
                    val viewModel = viewModel<ProfileViewModel>(
                        factory = viewModelFactory {
                            initializer {
                                val profileRepository = ProfileRepositoryImpl(
                                    context = context,
                                    userLocalDataSource = UserPreferences(SembakoApplication.module.application.dataStore),
                                )
                                ProfileViewModel(profileRepository)
                            }
                        }
                    )

                    val state by viewModel.state.collectAsStateWithLifecycle()

                    ProfileScreen(
                        state = state,
                        onEvent = viewModel::onEvent,
                        onNavigate = {
                            when (it) {
                                null -> navController.navigateUp()
                                is DashboardDestination -> navController.navigate(
                                    DashboardDestination
                                ) {
                                    popUpTo(DashboardDestination) { inclusive = true }
                                }

                                else -> navController.navigate(it)
                            }
                        },
                    )
                }
                composable<StatisticsDestination> {
                    val viewModel = viewModel<StatisticsViewModel>(
                        factory = viewModelFactory {
                            initializer {
                                val repository = StatisticsRepositoryImpl(
                                    remoteDataSource = SembakoApplication.module.apiService,
                                    userPreferences = SembakoApplication.module.userPreferences,
                                    localDataSource = SembakoApplication.module.statisticsPreferences
                                )
                                StatisticsViewModel(repository)
                            }
                        }
                    )
                    val state by viewModel.state.collectAsStateWithLifecycle()

                    StatisticsScreen(
                        state = state,
                        onEvent = viewModel::onEvent,
                        onNavigateUp = { navController.navigateUp() },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedContentScope = this@composable
                    )
                }
                composable<HistoryDestination> {
                    val viewModel = viewModel<HistoryViewModel>(
                        factory = viewModelFactory {
                            initializer {
                                val repository = HistoryRepositoryImpl(
                                    userPreferences = SembakoApplication.module.userPreferences,
                                    remoteDataSource = SembakoApplication.module.apiService,
                                    localDataSource = SembakoApplication.module.historyDao,
                                    storeRemoteDataSource = SembakoApplication.module.storeDataSource
                                )
                                HistoryViewModel(repository)
                            }
                        }
                    )
                    val state by viewModel.state.collectAsStateWithLifecycle()

                    HistoryScreen(
                        state = state,
                        onEvent = viewModel::onEvent,
                        onNavigateUp = { navController.navigateUp() },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedContentScope = this@composable
                    )
                }
                composable<AboutDestination> {
                    AboutScreenRoot(navController)
                }
                composable<FormDestination> {
                    val args = it.toRoute<FormDestination>()

                    val viewModel = viewModel<GroceryFormViewModel>(
                        factory = viewModelFactory {
                            initializer {
                                val repository = GroceryFormRepositoryImpl(
                                    localDataSource = SembakoApplication.module.sembakoDao,
                                    remoteDataSource = SembakoApplication.module.groceryDataSource,
                                    userPreferences = SembakoApplication.module.userPreferences
                                )
                                GroceryFormViewModel(repository)
                            }
                        }
                    )
                    LaunchedEffect(true) {
                        viewModel.setId(args.id, args.storeId)
                    }
                    val state by viewModel.state.collectAsStateWithLifecycle()

                    GroceryFormScreen(
                        state = state,
                        onEvent = viewModel::onEvent,
                        onNavigateBack = { navController.navigateUp() }
                    )
                }
                composable<StoreDestination> {
                    val viewModel = viewModel<StoreListViewModel>(
                        factory = viewModelFactory {
                            initializer {
                                val repository = StoreRepositoryImpl(
                                    dataSource = SembakoApplication.module.storeDataSource,
                                    userLocalDataSource = SembakoApplication.module.userPreferences
                                )
                                StoreListViewModel(repository)
                            }
                        }
                    )
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    StoreListScreen(
                        state = state,
                        onEvent = viewModel::onEvent,
                        onNavigateBack = { navController.navigateUp() },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedContentScope = this@composable
                    )
                }
            }
        )
    }
}
