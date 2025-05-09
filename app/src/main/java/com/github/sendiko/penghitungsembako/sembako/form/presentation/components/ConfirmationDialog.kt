package com.github.sendiko.penghitungsembako.sembako.form.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.sendiko.penghitungsembako.R

@Composable
fun ConfirmationDialog(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            Text(text = title)
        },
        text = {
            Text(text = message)
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(
                    text = stringResource(R.string.delete),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}

@Preview
@Composable
private fun ConfirmationDialogPrev() {
    MaterialTheme {
        ConfirmationDialog(
            title = "Hapus sembako ini?",
            message = "Data akan hilang selamanya",
            onConfirm = { },
            onDismiss = { }
        )
    }
}