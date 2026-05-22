package id.my.sendiko.sembako.store.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Store
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.my.sendiko.sembako.R
import id.my.sendiko.sembako.store.domain.Store

@Composable
fun StoreCard(
    modifier: Modifier = Modifier,
    store: Store
) {
    Card(modifier) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                imageVector = Icons.Rounded.Store,
                contentDescription = stringResource(R.string.store_info)
            )
            Spacer(
                modifier = Modifier.width(4.dp)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = store.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = store.address,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StoreCardPreview() {
    StoreCard(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        store = Store(
            id = 0,
            name = "Sendiko's Sembako",
            address = "Bumi Cikoneng Indah B10, Jl. Cikoneng.",
            phone = "082241626760",
            email = "sembako@sendiko.my.id"
        )
    )
}