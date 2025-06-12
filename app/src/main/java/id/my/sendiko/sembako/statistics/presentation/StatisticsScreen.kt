package id.my.sendiko.sembako.statistics.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import id.my.sendiko.sembako.R
import id.my.sendiko.sembako.core.ui.util.toRupiah
import id.my.sendiko.sembako.profile.presentation.component.StatsCard
import com.sendiko.content_box_with_notification.ContentBoxWithNotification
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    state: StatisticsState,
    onEvent: (StatisticsEvent) -> Unit,
    onNavigateUp: () -> Unit
) {
    LaunchedEffect(state.statistics) {
        if (state.statistics == null)
            onEvent(StatisticsEvent.LoadData)
    }

    LaunchedEffect(state.message) {
        if (state.message.isNotBlank()){
            delay(2000)
            onEvent(StatisticsEvent.ClearState)
        }
    }

    ContentBoxWithNotification(
        message = state.message,
        isLoading = state.isLoading,
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = stringResource(R.string.statistics))
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = onNavigateUp
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = stringResource(R.string.back)
                                )
                            }
                        }
                    )
                }
            ) { paddingValues ->
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    contentPadding = PaddingValues(
                        top = paddingValues.calculateTopPadding() + 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                    verticalItemSpacing = 16.dp,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item(
                        span = StaggeredGridItemSpan.FullLine
                    ) {
                        StatsCard(
                            label = stringResource(R.string.total_income),
                            statistics = if (state.isLoading) stringResource(R.string.loading)
                                else state.statistics?.totalSales.toString().toRupiah(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        )
                    }
                    item{
                        StatsCard(
                            label = stringResource(R.string.total_groceries),
                            statistics = if (state.isLoading) stringResource(R.string.loading) else state.statistics?.groceryCount.toString(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                    item {
                        StatsCard(
                            label = stringResource(R.string.total_transaction),
                            statistics = if (state.isLoading) stringResource(R.string.loading) else state.statistics?.totalHistory.toString(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        )
                    }
                }
            }
        }
    )
}