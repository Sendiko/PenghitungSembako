package id.my.sendiko.sembako.grocery.list.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import id.my.sendiko.sembako.R
import id.my.sendiko.sembako.core.ui.component.CustomTextField
import id.my.sendiko.sembako.core.ui.util.toRupiah
import id.my.sendiko.sembako.grocery.list.presentation.GroceryListEvent
import id.my.sendiko.sembako.grocery.list.presentation.GroceryListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryModalBottomSheet(
    state: GroceryListState,
    onEvent: (GroceryListEvent) -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = {
            onEvent(GroceryListEvent.OnDismiss)
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            state.grocery?.let {
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
                            it.pricePerUnit.toString().toRupiah(),
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
                    onEvent(GroceryListEvent.OnQuantityChange(it))
                },
                label = stringResource(R.string.quantity),
                trailingIcon = {
                    Text(
                        text = state.grocery?.unit ?: "",
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
                    text = stringResource(
                        R.string.total_price,
                        state.totalPrice.toString().dropLast(1).toRupiah()
                    ),
                    style = MaterialTheme.typography.headlineMedium
                )
                IconButton(
                    onClick = onShareClick
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
                if (state.totalPrice != 0.0) {
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                            onEvent(GroceryListEvent.OnSaveTransaction)
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
                        onEvent(GroceryListEvent.OnCalculateClick)
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
