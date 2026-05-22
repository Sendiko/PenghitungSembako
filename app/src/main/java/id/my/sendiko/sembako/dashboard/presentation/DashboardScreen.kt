package id.my.sendiko.sembako.dashboard.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PieChart
import androidx.compose.material.icons.rounded.Receipt
import androidx.compose.material.icons.rounded.Store
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sendiko.content_box_with_notification.ContentBoxWithNotification
import id.my.sendiko.sembako.R
import id.my.sendiko.sembako.core.navigation.HistoryDestination
import id.my.sendiko.sembako.core.navigation.ListDestination
import id.my.sendiko.sembako.core.navigation.ProfileDestination
import id.my.sendiko.sembako.core.navigation.StatisticsDestination
import id.my.sendiko.sembako.core.navigation.StoreDestination
import id.my.sendiko.sembako.core.ui.theme.bodyFontFamily
import id.my.sendiko.sembako.dashboard.presentation.components.LongMenuCard
import id.my.sendiko.sembako.dashboard.presentation.components.MenuCard
import id.my.sendiko.sembako.store.list.presentation.StoreModalBottomSheet
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    state: DashboardState,
    onEvent: (DashboardEvent) -> Unit,
    signInEventFlow: Flow<Unit>,
    onNavigate: (Any) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val context = LocalContext.current

    LaunchedEffect(key1 = signInEventFlow) {
        signInEventFlow.collectLatest {
            val result = GoogleAuthUI.interactiveSignIn(context)
            onEvent(DashboardEvent.OnResult(result))
        }
    }

    LaunchedEffect(state.isSignInSuccessful) {
        if (state.isSignInSuccessful) {
            delay(2000)
            onEvent(DashboardEvent.ClearState)
        }
    }

    LaunchedEffect(state.message) {
        if (state.message.isNotBlank()) {
            delay(2000)
            onEvent(DashboardEvent.ClearState)
        }
    }

    ContentBoxWithNotification(
        isLoading = state.isLoading,
        message = state.message,
        isErrorNotification = state.isError,
        textStyle = TextStyle(
            fontFamily = bodyFontFamily
        ),
        content = {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        scrollBehavior = scrollBehavior,
                        title = {
                            Text(
                                text = stringResource(R.string.app_name),
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        actions = {
                            IconButton(
                                onClick = { onNavigate(ProfileDestination) }
                            ) {
                                if (state.user.username.isBlank()) {
                                    Icon(
                                        imageVector = Icons.Rounded.Person,
                                        contentDescription = stringResource(R.string.profile)
                                    )
                                } else {
                                    AsyncImage(
                                        modifier = Modifier
                                            .clip(CircleShape),
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(state.user.profileUrl)
                                            .crossfade(true)
                                            .build(),
                                        error = painterResource(R.drawable.baseline_broken_image_24),
                                        contentScale = ContentScale.Crop,
                                        contentDescription = state.user.username,
                                    )
                                }
                            }
                        }
                    )
                }
            ) { paddingValues ->
                LazyVerticalStaggeredGrid(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    contentPadding = PaddingValues(
                        top = paddingValues.calculateTopPadding() + 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = paddingValues.calculateBottomPadding() + 16.dp
                    ),
                    columns = StaggeredGridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalItemSpacing = 16.dp
                ) {

                    if (!state.user.hasStore) {
                        item(span = StaggeredGridItemSpan.FullLine) {
                            LongMenuCard(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { onEvent(DashboardEvent.OnStoreSheetVisible(true)) }
                            )
                        }
                    }

                    item {
                        MenuCard(
                            onClick = { onNavigate(ListDestination) },
                            icon = {
                                Icon(
                                    modifier = Modifier.size(64.dp),
                                    painter = painterResource(R.drawable.grocery),
                                    contentDescription = stringResource(R.string.your_grocery)
                                )
                            },
                            text = stringResource(R.string.your_grocery)
                        )
                    }

                    if (state.user.hasStore) {
                        item {
                            MenuCard(
                                onClick = { onNavigate(StoreDestination) },
                                icon = {
                                    Icon(
                                        modifier = Modifier.size(64.dp),
                                        imageVector = Icons.Rounded.Store,
                                        contentDescription = stringResource(R.string.store_info)
                                    )
                                },
                                text = stringResource(R.string.your_store)
                            )
                        }
                    }

                    item {
                        MenuCard(
                            onClick = { onNavigate(StatisticsDestination) },
                            icon = {
                                Icon(
                                    modifier = Modifier.size(64.dp),
                                    imageVector = Icons.Rounded.PieChart,
                                    contentDescription = stringResource(R.string.statistics)
                                )
                            },
                            text = stringResource(R.string.statistics)
                        )
                    }

                    if (state.user.username.isBlank()) {
                        item {
                            MenuCard(
                                onClick = {
                                    onEvent(DashboardEvent.OnLoginClicked)
                                },
                                icon = {
                                    Icon(
                                        modifier = Modifier.size(64.dp),
                                        imageVector = Icons.Rounded.Person,
                                        contentDescription = stringResource(R.string.profile)
                                    )
                                },
                                text = stringResource(R.string.login_title)
                            )
                        }
                    }

                    item {
                        MenuCard(
                            onClick = { onNavigate(HistoryDestination) },
                            icon = {
                                Icon(
                                    modifier = Modifier.size(64.dp),
                                    imageVector = Icons.Rounded.Receipt,
                                    contentDescription = stringResource(R.string.transaction)
                                )
                            },
                            text = stringResource(R.string.transaction)
                        )
                    }

                }
            }
        }
    )

    if (state.isStoreSheetVisible) {
        StoreModalBottomSheet(
            onDismissRequest = { onEvent(DashboardEvent.OnStoreSheetVisible(false)) },
            onSaveClick = { onEvent(DashboardEvent.OnSaveStore) },
            storeName = state.storeName,
            onStoreNameChange = { onEvent(DashboardEvent.OnStoreNameChange(it)) },
            storeAddress = state.storeAddress,
            onStoreAddressChange = { onEvent(DashboardEvent.OnStoreAddressChange(it)) },
            storePhone = state.storePhone,
            onStorePhoneChange = { onEvent(DashboardEvent.OnStorePhoneChange(it)) },
            storeEmail = state.storeEmail,
            onStoreEmailChange = { onEvent(DashboardEvent.OnStoreEmailChange(it)) }
        )
    }

}

@Preview(showSystemUi = true)
@Composable
private fun DashboardScreenPrev() {
    DashboardScreen(
        state = DashboardState(),
        onNavigate = { },
        onEvent = { },
        signInEventFlow = flow { }
    )
}