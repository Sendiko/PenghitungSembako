package com.github.sendiko.penghitungsembako.dashboard.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.sendiko.penghitungsembako.R
import com.github.sendiko.penghitungsembako.core.navigation.AboutDestination
import com.github.sendiko.penghitungsembako.core.ui.component.CustomTextField
import com.github.sendiko.penghitungsembako.dashboard.presentation.component.SembakoCard

@Composable
fun DashboardScreenRoot(
    navController: NavHostController,
) {

    val viewModel = viewModel { DashboardViewModel() }
    val state by viewModel.state.collectAsStateWithLifecycle()

    DashboardScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigate = {
            navController.navigate(it)
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    state: DashboardState,
    onEvent: (DashboardEvent) -> Unit,
    onNavigate: (Any) -> Unit,
) {
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
                actions = {
                    IconButton(
                        onClick = { onNavigate(AboutDestination) }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = null
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
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = stringResource(R.string.sembako_harga, it.pricePerUnit, it.unit),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
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
                                text = stringResource(R.string.kg),
                                fontWeight = FontWeight.Black
                            )
                        },
                        message = state.message
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(R.string.total_price, state.totalPrice),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        onClick = {
                            onEvent(DashboardEvent.OnCalculateClick)
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.count)
                        )
                    }
                }
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding(),
                start = 16.dp,
                end = 16.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.sembako) { sembako ->
                SembakoCard(
                    sembako = sembako,
                    onClick = {
                        onEvent(DashboardEvent.OnSembakoClick(sembako))
                    }
                )
            }
        }
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