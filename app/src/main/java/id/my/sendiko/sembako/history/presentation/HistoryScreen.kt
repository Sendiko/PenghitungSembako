package id.my.sendiko.sembako.history.presentation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.sendiko.content_box_with_notification.ContentBoxWithNotification
import id.my.sendiko.sembako.R
import id.my.sendiko.sembako.core.ui.theme.bodyFontFamily
import id.my.sendiko.sembako.core.ui.util.toRupiah
import id.my.sendiko.sembako.grocery.list.presentation.components.StoreSelector
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun HistoryScreen(
    state: HistoryState,
    onEvent: (HistoryEvent) -> Unit,
    onNavigateUp: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    LaunchedEffect(state.user) {
        if (state.user != null) {
            onEvent(HistoryEvent.LoadData)
        }
    }

    LaunchedEffect(state.message) {
        if (state.message.isNotBlank()){
            delay(2000)
            onEvent(HistoryEvent.ClearState)
        }
    }

    ContentBoxWithNotification(
        message = state.message,
        isLoading = state.isLoading,
        textStyle = TextStyle(fontFamily = bodyFontFamily),
        content = {
            with(sharedTransitionScope) {
                Scaffold(
                    modifier = Modifier.sharedBounds(
                        rememberSharedContentState(key = "history"),
                        animatedVisibilityScope = animatedContentScope
                    ),
                    topBar = {
                        TopAppBar(
                            scrollBehavior = scrollBehavior,
                            title = {
                                Text(
                                    text = stringResource(R.string.history)
                                )
                            },
                            navigationIcon = {
                                IconButton(
                                    onClick = onNavigateUp
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                        contentDescription = stringResource(R.string.back)
                                    )
                                }
                            }
                        )
                    }
                ) { paddingValues ->
                    LazyColumn(
                        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                        contentPadding = PaddingValues(
                            top = paddingValues.calculateTopPadding(),
                            end = 16.dp,
                            start = 16.dp,
                            bottom = 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            StoreSelector(
                                selectedStore = state.selectedStore,
                                stores = state.stores,
                                onStoreChange = {
                                    onEvent(HistoryEvent.OnStoreChange(it))
                                }
                            )
                        }
                        items(state.histories.reversed()) { history ->
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.End
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = history.groceryName,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = history.quantity.toString(),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                                HorizontalDivider()
                                Text(
                                    text = history.totalPrice.toString().toRupiah(),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    )

}
