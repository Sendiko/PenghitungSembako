package id.my.sendiko.sembako.grocery.list.presentation

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.sendiko.content_box_with_notification.ContentBoxWithNotification
import id.my.sendiko.sembako.R
import id.my.sendiko.sembako.core.navigation.FormDestination
import id.my.sendiko.sembako.core.preferences.UiMode
import id.my.sendiko.sembako.core.ui.theme.bodyFontFamily
import id.my.sendiko.sembako.grocery.core.presentation.GroceryCard
import id.my.sendiko.sembako.grocery.list.presentation.components.GroceryModalBottomSheet
import id.my.sendiko.sembako.grocery.list.presentation.components.StoreSelector
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    state: ListState,
    onEvent: (ListEvent) -> Unit,
    onNavigate: (Any?) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val context = LocalContext.current

    LaunchedEffect(state.message) {
        if (state.message.isNotBlank()) {
            delay(2000)
            onEvent(ListEvent.ClearState)
        }
    }

    LaunchedEffect(state.user) {
        if (state.user != null) {
            onEvent(ListEvent.LoadData)
        }
    }

    ContentBoxWithNotification(
        isLoading = state.isLoading,
        message = state.message,
        textStyle = TextStyle(
            fontFamily = bodyFontFamily
        ),
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        scrollBehavior = scrollBehavior,
                        title = {
                            Text(text = stringResource(R.string.your_grocery))
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = { onNavigate(null) }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = stringResource(R.string.back)
                                )
                            }
                        }
                    )
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        onClick = { onNavigate(FormDestination(null, state.selectedStore?.id)) },
                        text = {
                            Text(
                                text = stringResource(R.string.create_title)
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.create_title)
                            )
                        }
                    )
                }
            ) { paddingValues ->
                AnimatedVisibility(
                    visible = state.grocery != null
                ) {
                    GroceryModalBottomSheet(
                        state = state,
                        onEvent = onEvent,
                        onShareClick = {
                            shareData(
                                context = context,
                                message = context.getString(
                                    R.string.share,
                                    state.grocery?.name ?: "",
                                    state.grocery?.pricePerUnit ?: 0,
                                    state.quantity.toDoubleOrNull() ?: 0.0,
                                    state.totalPrice
                                )
                            )
                        }
                    )
                }
                AnimatedVisibility(
                    visible = state.groceries.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    LazyVerticalStaggeredGrid(
                        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                        columns = StaggeredGridCells.Fixed(2),
                        contentPadding = PaddingValues(
                            top = paddingValues.calculateTopPadding() + 16.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 128.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalItemSpacing = 16.dp
                    ) {
                        item(
                            span = StaggeredGridItemSpan.FullLine
                        ) {
                            StoreSelector(
                                selectedStore = state.selectedStore,
                                stores = state.stores,
                                onStoreChange = {
                                    onEvent(ListEvent.OnStoreChange(it))
                                }
                            )
                        }
                        item(
                            span = StaggeredGridItemSpan.FullLine
                        ) {
                            Box(
                                contentAlignment = Alignment.CenterStart
                            ) {
                                UiModeSelector(
                                    modifier = Modifier.wrapContentSize(),
                                    selectedUiMode = state.uiMode,
                                    onUiModeChange = {
                                        onEvent(ListEvent.SetPreference(it))
                                    }
                                )
                            }
                        }
                        items(
                            items = state.groceries,
                            span = {
                                if (state.uiMode == UiMode.LIST)
                                    StaggeredGridItemSpan.FullLine
                                else StaggeredGridItemSpan.SingleLane
                            }
                        ) { sembako ->
                            GroceryCard(
                                grocery = sembako,
                                onClick = {
                                    onEvent(ListEvent.OnGroceryChange(sembako))
                                },
                                onEdit = {
                                    onNavigate(FormDestination(sembako.id, state.selectedStore?.id))
                                }
                            )
                        }
                    }
                }
                AnimatedVisibility(
                    visible = state.groceries.isEmpty(),
                    modifier = Modifier.padding(paddingValues),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier.size(256.dp),
                            painter = painterResource(R.drawable.empty),
                            contentDescription = stringResource(R.string.empty)
                        )
                    }
                }
            }
        }
    )

}

private fun shareData(context: Context, message: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}