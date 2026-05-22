package id.my.sendiko.sembako.store.list.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import id.my.sendiko.sembako.R
import id.my.sendiko.sembako.core.ui.component.CustomTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreModalBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    onDismissRequest: () -> Unit,
    onSaveClick: () -> Unit,
    storeName: String,
    onStoreNameChange: (String) -> Unit,
    storeAddress: String,
    onStoreAddressChange: (String) -> Unit,
    storePhone: String,
    onStorePhoneChange: (String) -> Unit,
    storeEmail: String,
    onStoreEmailChange: (String) -> Unit,
    onDeleteClick: (() -> Unit)? = null,
) {
    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        dragHandle = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Surface(
                    modifier = Modifier
                        .size(width = 32.dp, height = 4.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = MaterialTheme.shapes.extraLarge
                ) {}
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    onDeleteClick?.let {
                        IconButton(
                            onClick = it
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Delete,
                                contentDescription = stringResource(R.string.delete),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.store_info),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            CustomTextField(
                value = storeName,
                onValueChange = onStoreNameChange,
                label = stringResource(R.string.store_name),
                hint = stringResource(R.string.store_name_hint),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Store,
                        contentDescription = stringResource(R.string.cd_store)
                    )
                }
            )
            CustomTextField(
                value = storeAddress,
                onValueChange = onStoreAddressChange,
                label = stringResource(R.string.store_address),
                hint = stringResource(R.string.store_address_hint),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = stringResource(R.string.cd_location)
                    )
                }
            )
            CustomTextField(
                value = storePhone,
                onValueChange = onStorePhoneChange,
                label = stringResource(R.string.store_phone),
                hint = stringResource(R.string.store_phone_hint),
                keyboardType = KeyboardType.Phone,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = stringResource(R.string.cd_phone)
                    )
                }
            )
            CustomTextField(
                value = storeEmail,
                onValueChange = onStoreEmailChange,
                label = stringResource(R.string.store_email),
                hint = stringResource(R.string.store_email_hint),
                keyboardType = KeyboardType.Email,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = stringResource(R.string.cd_email)
                    )
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onSaveClick,
                shape = RoundedCornerShape(100)
            ) {
                Text(text = stringResource(R.string.create_label))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
