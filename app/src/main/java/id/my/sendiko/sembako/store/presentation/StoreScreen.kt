package id.my.sendiko.sembako.store.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sendiko.content_box_with_notification.ContentBoxWithNotification
import id.my.sendiko.sembako.R
import id.my.sendiko.sembako.store.presentation.components.StoreCard
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreScreen(
    state: StoreState,
    onEvent: (StoreEvent) -> Unit,
    onNavigateBack: () -> Unit
) {

    LaunchedEffect(state.message) {
        if (state.message.isNotBlank()) {
            delay(2000)
            onEvent(StoreEvent.OnClearState)
        }
    }

    LaunchedEffect(state.user.id) {
        if (state.user.id != 0) {
            onEvent(StoreEvent.OnLoadData)
        }
    }

    ContentBoxWithNotification(
        message = state.message,
        isLoading = state.isLoading,
        isErrorNotification = state.isError,
        content = {
            Scaffold(
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
                }
            ) { paddingValues ->
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
                            store = store
                        )
                    }
                }
            }
        }
    )

}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun StoreCardPrev() {
    StoreScreen(
        state = StoreState(),
        onEvent = { },
        onNavigateBack = { }
    )
}