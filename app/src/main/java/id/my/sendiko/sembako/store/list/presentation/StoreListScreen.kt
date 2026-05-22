package id.my.sendiko.sembako.store.list.presentation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sendiko.content_box_with_notification.ContentBoxWithNotification
import id.my.sendiko.sembako.R
import id.my.sendiko.sembako.store.list.presentation.components.StoreCard
import id.my.sendiko.sembako.store.list.presentation.components.StoreModalBottomSheet
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun StoreListScreen(
    state: StoreListState,
    onEvent: (StoreListEvent) -> Unit,
    onNavigateBack: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {

    LaunchedEffect(state.message) {
        if (state.message.isNotBlank()) {
            delay(2000)
            onEvent(StoreListEvent.OnClearState)
        }
    }

    LaunchedEffect(state.user.id) {
        if (state.user.id != 0) {
            onEvent(StoreListEvent.OnLoadData)
        }
    }

    ContentBoxWithNotification(
        message = state.message,
        isLoading = state.isLoading,
        isErrorNotification = state.isError,
        content = {
            with(sharedTransitionScope) {
                Scaffold(
                    modifier = Modifier.sharedBounds(
                        rememberSharedContentState(key = "store_list"),
                        animatedVisibilityScope = animatedContentScope
                    ),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(stringResource(R.string.your_store))
                            },
                            navigationIcon = {
                                IconButton(
                                    onClick = onNavigateBack
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                        contentDescription = stringResource(R.string.back)
                                    )
                                }
                            }
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                onEvent(StoreListEvent.OnStoreSheetVisible(true))
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = stringResource(R.string.create_title)
                            )
                        }
                    }
                ) { paddingValues ->
                    if (state.isDeleteDialogOpen) {
                        AlertDialog(
                            onDismissRequest = {
                                onEvent(StoreListEvent.OnDeleteDialogOpen(false))
                            },
                            title = {
                                Text(text = stringResource(R.string.delete_store_title))
                            },
                            text = {
                                Text(text = stringResource(R.string.delete_store_message))
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        onEvent(StoreListEvent.OnDeleteStore)
                                        onEvent(StoreListEvent.OnDeleteDialogOpen(false))
                                    }
                                ) {
                                    Text(text = stringResource(R.string.delete))
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = {
                                        onEvent(StoreListEvent.OnDeleteDialogOpen(false))
                                    }
                                ) {
                                    Text(text = stringResource(R.string.cancel))
                                }
                            }
                        )
                    }
                    if (state.isStoreSheetVisible) {
                        StoreModalBottomSheet(
                            onDismissRequest = {
                                onEvent(StoreListEvent.OnStoreSheetVisible(false))
                            },
                            onSaveClick = {
                                onEvent(StoreListEvent.OnSaveStore)
                            },
                            storeName = state.storeName,
                            onStoreNameChange = {
                                onEvent(StoreListEvent.OnStoreNameChange(it))
                            },
                            storeAddress = state.storeAddress,
                            onStoreAddressChange = {
                                onEvent(StoreListEvent.OnStoreAddressChange(it))
                            },
                            storePhone = state.storePhone,
                            onStorePhoneChange = {
                                onEvent(StoreListEvent.OnStorePhoneChange(it))
                            },
                            storeEmail = state.storeEmail,
                            onStoreEmailChange = {
                                onEvent(StoreListEvent.OnStoreEmailChange(it))
                            },
                            onDeleteClick = if (state.selectedStoreId != null) {
                                { onEvent(StoreListEvent.OnDeleteDialogOpen(true)) }
                            } else null
                        )
                    }
                    LazyColumn(
                        contentPadding = PaddingValues(
                            top = paddingValues.calculateTopPadding() + 16.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = paddingValues.calculateBottomPadding() + 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.stores) { store ->
                            StoreCard(
                                modifier = Modifier.fillMaxWidth(),
                                store = store,
                                onClick = {
                                    onEvent(StoreListEvent.OnEditStore(store))
                                }
                            )
                        }
                    }
                }
            }
        }
    )

}
