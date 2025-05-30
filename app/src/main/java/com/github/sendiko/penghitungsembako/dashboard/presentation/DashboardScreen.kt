package com.github.sendiko.penghitungsembako.dashboard.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.sendiko.penghitungsembako.R
import com.github.sendiko.penghitungsembako.core.navigation.ListDestination
import com.github.sendiko.penghitungsembako.core.navigation.ProfileDestination
import com.github.sendiko.penghitungsembako.core.ui.theme.bodyFontFamily
import com.sendiko.content_box_with_notification.ContentBoxWithNotification

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    state: DashboardState,
    onNavigate: (Any) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    ContentBoxWithNotification(
        isLoading = state.isLoading,
        message = state.message,
        textStyle = TextStyle(
            fontFamily = bodyFontFamily
        ),
        content = {
            Scaffold(
                topBar = {
                    LargeTopAppBar(
                        scrollBehavior = scrollBehavior,
                        title = {
                            Text(text = stringResource(R.string.greeting,
                                (state.user?.username ?: "").split(" ")[0]
                            ))
                        },
                    )
                }
            ) { paddingValues ->
                LazyVerticalStaggeredGrid(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    contentPadding = PaddingValues(
                        top = paddingValues.calculateTopPadding() + 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = paddingValues.calculateBottomPadding()+ 16.dp
                    ),
                    columns = StaggeredGridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalItemSpacing = 16.dp
                ) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            onClick = { onNavigate(ListDestination) }
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Icon(
                                    modifier = Modifier.size(64.dp),
                                    painter = painterResource(R.drawable.grocery),
                                    contentDescription = stringResource(R.string.your_grocery)
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = stringResource(R.string.your_grocery),
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    }
                    item {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            ),
                            onClick = { onNavigate(ListDestination) }
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Icon(
                                    modifier = Modifier.size(64.dp),
                                    imageVector = Icons.Filled.PieChart,
                                    contentDescription = stringResource(R.string.statistics)
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = stringResource(R.string.statistics),
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    }
                    item {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                            ),
                            onClick = { onNavigate(ProfileDestination) }
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                AsyncImage(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(64.dp),
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(state.user?.profileUrl)
                                        .crossfade(true)
                                        .build(),
                                    error = painterResource(R.drawable.baseline_broken_image_24),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = state.user?.username,
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = stringResource(R.string.profile),
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    }
                    item {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            ),
                            onClick = { onNavigate(ListDestination) }
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Icon(
                                    modifier = Modifier.size(64.dp),
                                    imageVector = Icons.Default.Receipt,
                                    contentDescription = stringResource(R.string.transaction)
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = stringResource(R.string.transaction),
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    )

}

@Preview(showSystemUi = true)
@Composable
private fun DashboardScreenPrev() {
    DashboardScreen(
        state = DashboardState(),
        onNavigate = { }
    )
}