package com.github.sendiko.penghitungsembako.sembako.dashboard.presentation

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ViewList
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.sendiko.penghitungsembako.R
import com.github.sendiko.penghitungsembako.core.navigation.FormDestination
import com.github.sendiko.penghitungsembako.core.navigation.ProfileDestination
import com.github.sendiko.penghitungsembako.core.preferences.UiMode
import com.github.sendiko.penghitungsembako.core.ui.component.CustomTextField
import com.github.sendiko.penghitungsembako.sembako.core.presentation.SembakoCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    state: DashboardState,
    onEvent: (DashboardEvent) -> Unit,
    onNavigate: (Any) -> Unit,
) {

    LaunchedEffect(state.sembako) {
        if (state.sembako.isEmpty())
            onEvent(DashboardEvent.LoadData)
    }

    DisposableEffect(Unit) {
        onDispose {
            onEvent(DashboardEvent.ClearState)
        }
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val context = LocalContext.current
    Scaffold(
        topBar = {
            LargeTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(text = stringResource(R.string.greeting,
                        (state.user?.username ?: "").split(" ")[0]
                    ))
                },
                actions = {
                    IconButton(
                        onClick = { onNavigate(ProfileDestination) }
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .clip(CircleShape),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(state.user?.profileUrl)
                                .crossfade(true)
                                .build(),
                            error = painterResource(R.drawable.baseline_broken_image_24),
                            contentScale = ContentScale.Crop,
                            contentDescription = state.user?.username,
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { onNavigate(FormDestination(null)) },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.create)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onEvent(
                                DashboardEvent.SetPreference(
                                    if (state.uiMode == UiMode.GRID)
                                        UiMode.LIST
                                    else UiMode.GRID
                                )
                            )
                        }
                    ) {
                        Icon(
                            imageVector = if (state.uiMode == UiMode.LIST)
                                Icons.AutoMirrored.Outlined.ViewList
                            else Icons.Outlined.GridView,
                            contentDescription = if (state.uiMode == UiMode.LIST)
                                stringResource(R.string.list)
                            else stringResource(R.string.grid)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        AnimatedVisibility(
            visible = state.selectedSembako != null
        ) {
            ModalBottomSheet(
                onDismissRequest = {
                    onEvent(DashboardEvent.OnDismiss)
                }
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    state.selectedSembako?.let {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = stringResource(
                                    R.string.sembako_harga,
                                    it.pricePerUnit,
                                    it.unit
                                ),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                    CustomTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        value = state.quantity,
                        onValueChange = {
                            onEvent(DashboardEvent.OnQuantityChange(it))
                        },
                        label = stringResource(R.string.quantity),
                        trailingIcon = {
                            Text(
                                text = state.selectedSembako?.unit ?: "",
                                fontWeight = FontWeight.Black
                            )
                        },
                        message = state.message,
                        keyboardType = KeyboardType.Number
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .weight(1f),
                            text = stringResource(R.string.total_price, state.totalPrice),
                            style = MaterialTheme.typography.headlineMedium
                        )
                        IconButton(
                            onClick = {
                                shareData(
                                    context = context,
                                    message = context.getString(
                                        R.string.share,
                                        state.selectedSembako?.name ?: "",
                                        state.selectedSembako?.pricePerUnit ?: 0.0,
                                        state.quantity.toDoubleOrNull() ?: 0.0,
                                        state.totalPrice
                                    )
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AnimatedVisibility(
                            modifier = Modifier
                                .weight(1f),
                            visible = state.totalPrice != 0.0
                        ) {
                            OutlinedButton(
                                shape = RoundedCornerShape(16.dp),
                                onClick = {

                                },
                                contentPadding = PaddingValues(vertical = 16.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.save),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Button(
                            modifier = Modifier
                                .weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            onClick = {
                                onEvent(DashboardEvent.OnCalculateClick)
                            },
                            contentPadding = PaddingValues(vertical = 16.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.count),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = state.sembako.isNotEmpty(),
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
                    bottom = paddingValues.calculateBottomPadding()+ 16.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalItemSpacing = 16.dp
            ) {
                items(
                    items = state.sembako,
                    span = {
                        if (state.uiMode == UiMode.LIST)
                            StaggeredGridItemSpan.FullLine
                        else StaggeredGridItemSpan.SingleLane
                    }
                ) { sembako ->
                    SembakoCard(
                        sembako = sembako,
                        onClick = {
                            onEvent(DashboardEvent.OnSembakoClick(sembako))
                        },
                        onEdit = {
                            onNavigate(FormDestination(sembako.id))
                        }
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = state.sembako.isEmpty(),
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

private fun shareData(context: Context, message: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DashboardScreenPrev() {
    DashboardScreen(
        state = DashboardState(),
        onEvent = { },
        onNavigate = { }
    )
}