package id.my.sendiko.sembako.grocery.list.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import id.my.sendiko.sembako.R
import id.my.sendiko.sembako.store.domain.Store

@Composable
fun StoreSelector(
    modifier: Modifier = Modifier,
    selectedStore: Store?,
    stores: List<Store>,
    onStoreChange: (Store) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedStore?.name ?: stringResource(R.string.select_store),
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            readOnly = true,
            label = { Text(text = stringResource(R.string.selected_store)) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            },
            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                .also { interactionSource ->
                    androidx.compose.runtime.LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect {
                            if (it is androidx.compose.foundation.interaction.PressInteraction.Release) {
                                expanded = !expanded
                            }
                        }
                    }
                }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            stores.forEach { store ->
                DropdownMenuItem(
                    text = { Text(text = store.name) },
                    onClick = {
                        onStoreChange(store)
                        expanded = false
                    }
                )
            }
        }
    }
}
