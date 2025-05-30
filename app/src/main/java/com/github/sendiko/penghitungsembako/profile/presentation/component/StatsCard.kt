package com.github.sendiko.penghitungsembako.profile.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.sendiko.penghitungsembako.R
import com.github.sendiko.penghitungsembako.core.ui.theme.AppTheme
import com.github.sendiko.penghitungsembako.profile.presentation.utils.formatRupiah

@Composable
fun StatsCard(
    modifier: Modifier = Modifier,
    label: String,
    statistics: String,
    colors: CardColors
) {
    Card(
        modifier = modifier,
        colors = colors
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = statistics.toString(),
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Preview
@Composable
private fun StatsCardPrev() {
    AppTheme {
        Surface {
            StatsCard(
                modifier = Modifier.padding(16.dp),
                label = stringResource(R.string.total_transaction),
                statistics = formatRupiah(600000.00),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    }
}