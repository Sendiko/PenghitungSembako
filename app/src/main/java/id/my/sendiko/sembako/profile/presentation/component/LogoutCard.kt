package id.my.sendiko.sembako.profile.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LogoutCard(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
    text: String,
    icon: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onLogout,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            icon()
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}