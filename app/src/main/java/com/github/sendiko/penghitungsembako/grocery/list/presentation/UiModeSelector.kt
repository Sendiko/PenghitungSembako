package com.github.sendiko.penghitungsembako.grocery.list.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.sendiko.penghitungsembako.R
import com.github.sendiko.penghitungsembako.core.preferences.UiMode
import com.github.sendiko.penghitungsembako.core.ui.theme.AppTheme

@Composable
fun UiModeSelector(
    modifier: Modifier = Modifier,
    selectedUiMode: UiMode,
    onUiModeChange: (UiMode) -> Unit
) {
    Surface(
        modifier = modifier,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = CircleShape
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            UiMode.entries.forEach { uiMode ->
                IconButton(
                    onClick = { onUiModeChange(uiMode) },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = if (uiMode == selectedUiMode)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (uiMode == selectedUiMode)
                            MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Icon(
                        imageVector = if (uiMode == UiMode.LIST)
                            Icons.AutoMirrored.Default.ViewList
                        else Icons.Default.GridView,
                        contentDescription = if (uiMode == UiMode.LIST)
                            stringResource(R.string.list)
                        else stringResource(R.string.grid)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun UiModeSelectorPrev() {
    AppTheme {
        UiModeSelector(
            modifier = Modifier.padding(8.dp),
            selectedUiMode = UiMode.LIST,
            onUiModeChange = {}
        )
    }
}