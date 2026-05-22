package id.my.sendiko.sembako.dashboard.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MenuCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: (@Composable () -> Unit),
    text: String,
    enabled: Boolean = true,
) {
    val containerColors = listOf(
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.secondaryContainer,
        MaterialTheme.colorScheme.tertiaryContainer
    )
    val contentColors = listOf(
        MaterialTheme.colorScheme.onPrimaryContainer,
        MaterialTheme.colorScheme.onSecondaryContainer,
        MaterialTheme.colorScheme.onTertiaryContainer
    )
    val random = (0..2).random()
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = containerColors[random],
            contentColor = contentColors[random]
        ),
        onClick = onClick,
        enabled = enabled
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            icon()
            Spacer(Modifier.height(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}