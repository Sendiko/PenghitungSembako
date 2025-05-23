package com.github.sendiko.penghitungsembako.sembako.form.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.sendiko.penghitungsembako.R
import com.github.sendiko.penghitungsembako.core.ui.component.CustomTextField
import com.github.sendiko.penghitungsembako.sembako.form.presentation.components.ConfirmationDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    state: FormState,
    onEvent: (FormEvent) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(key1 = state.isSaved, key2 = state.isDeleted) {
        if (state.isSaved || state.isDeleted)
            onNavigateBack()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (state.id == null)
                            stringResource(R.string.create)
                        else stringResource(R.string.edit)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onEvent(FormEvent.OnSave) }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(R.string.create)
                        )
                    }
                    if (state.id != null) {
                        IconButton(
                            onClick = { onEvent(FormEvent.OnDeleteClicked((!state.isDeleting))) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.delete)
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        val contentPadding = PaddingValues(
            top = paddingValues.calculateTopPadding(),
            start = 16.dp,
            end = 16.dp
        )
        if (state.isDeleting) {
            ConfirmationDialog(
                title = stringResource(R.string.delete_title),
                message = stringResource(R.string.delete_message),
                onConfirm = {
                    onEvent(FormEvent.OnDelete)
                    onEvent(FormEvent.OnDeleteClicked(!state.isDeleting))
                },
                onDismiss = {
                    onEvent(FormEvent.OnDeleteClicked(!state.isDeleting))
                }
            )
        }
        LazyColumn(
            contentPadding = contentPadding,
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            item {
                CustomTextField(
                    value = state.name,
                    onValueChange = { onEvent(FormEvent.OnNameChanged(it)) },
                    label = stringResource(R.string.name),
                    isError = state.message.isNotBlank(),
                    message = state.message,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.TextFields,
                            contentDescription = stringResource(R.string.name)
                        )
                    }
                )
            }
            item {
                CustomTextField(
                    value = state.unit,
                    onValueChange = { onEvent(FormEvent.OnUnitChanged(it)) },
                    label = stringResource(R.string.unit),
                    isError = state.message.isNotBlank(),
                    message = state.message,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.weight),
                            contentDescription = stringResource(R.string.unit)
                        )
                    }
                )
            }
            item {
                CustomTextField(
                    value = state.pricePerUnit,
                    onValueChange = { onEvent(FormEvent.OnPricePerUnitChanged(it)) },
                    label = stringResource(R.string.price_per_unit),
                    isError = state.message.isNotBlank(),
                    message = state.message,
                    leadingIcon = {
                        Text(
                            text = stringResource(R.string.price),
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
private fun FormScreenPrev() {
    MaterialTheme {
        FormScreen(
            state = FormState(),
            onEvent = { },
            onNavigateBack = { }
        )
    }
}