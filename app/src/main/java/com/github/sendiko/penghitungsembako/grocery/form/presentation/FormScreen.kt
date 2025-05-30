package com.github.sendiko.penghitungsembako.grocery.form.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.github.sendiko.penghitungsembako.R
import com.github.sendiko.penghitungsembako.core.ui.component.CustomTextField
import com.github.sendiko.penghitungsembako.core.ui.theme.bodyFontFamily
import com.github.sendiko.penghitungsembako.grocery.form.presentation.components.ConfirmationDialog
import com.github.sendiko.penghitungsembako.grocery.form.presentation.components.GroceryImage
import com.sendiko.content_box_with_notification.ContentBoxWithNotification
import kotlinx.coroutines.delay
import kotlin.Unit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    state: FormState,
    onEvent: (FormEvent) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(key1 = state.isSaved, key2 = state.isDeleted) {
        if (state.isSaved || state.isDeleted){
            delay(1000)
            onNavigateBack()
        }
    }

    val launcher = rememberLauncherForActivityResult(CropImageContract()) {
        onEvent(
            FormEvent.OnImageChosen(
                bitmap = getCroppedImage(context.contentResolver, it)
            )
        )
    }

    ContentBoxWithNotification(
        isLoading = state.isLoading,
        message = state.message,
        textStyle = TextStyle(
            fontFamily = bodyFontFamily
        ),
        content = {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        scrollBehavior = scrollBehavior,
                        title = {
                            Text(
                                text = if (state.id == null)
                                    stringResource(R.string.create_title)
                                else stringResource(R.string.edit)
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = { onNavigateBack() }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = stringResource(R.string.back)
                                )
                            }
                        }
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
                        GroceryImage(
                            bitmap = state.bitmap,
                            imageUrl = state.imageUrl,
                            onLaunchImagePicker = {
                                val options = CropImageContractOptions(
                                    null,
                                    CropImageOptions(
                                        imageSourceIncludeGallery = true,
                                        imageSourceIncludeCamera = true,
                                        fixAspectRatio = true
                                    )
                                )
                                launcher.launch(options)
                            },
                            contentDescription = state.name
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    item {
                        CustomTextField(
                            value = state.name,
                            onValueChange = { onEvent(FormEvent.OnNameChanged(it)) },
                            label = stringResource(R.string.name),
                            isError = state.nameMessage.isNotBlank(),
                            message = state.nameMessage,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.TextFields,
                                    contentDescription = stringResource(R.string.name)
                                )
                            }
                        )
                    }
                    item {
                        ExposedDropdownMenuBox(
                            expanded = state.isExpanding,
                            onExpandedChange = { onEvent(FormEvent.OnDropDownChanged(it)) }
                        ) {
                            CustomTextField(
                                modifier =
                                    Modifier.menuAnchor(MenuAnchorType.PrimaryEditable),
                                value = state.unit,
                                onValueChange = { onEvent(FormEvent.OnUnitChanged(it)) },
                                label = stringResource(R.string.unit),
                                isError = state.unitMessage.isNotBlank(),
                                message = state.unitMessage,
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(R.drawable.weight),
                                        contentDescription = stringResource(R.string.unit)
                                    )
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        modifier =
                                            Modifier.menuAnchor(MenuAnchorType.PrimaryEditable),
                                        expanded = state.isExpanding,
                                    )
                                },
                            )
                            ExposedDropdownMenu(
                                expanded = state.isExpanding,
                                onDismissRequest = { onEvent(FormEvent.OnDropDownChanged(false)) }
                            ) {
                                com.github.sendiko.penghitungsembako.grocery.form.presentation.Unit.entries.forEach {
                                    DropdownMenuItem(
                                        text = { Text(it.name) },
                                        onClick = {
                                            onEvent(FormEvent.OnUnitChanged(it.name))
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                    )
                                }
                            }
                        }
                    }
                    item {
                        CustomTextField(
                            value = state.pricePerUnit,
                            onValueChange = { onEvent(FormEvent.OnPricePerUnitChanged(it)) },
                            label = stringResource(R.string.price_per_unit),
                            isError = state.pricePerUnitMessage.isNotBlank(),
                            message = state.pricePerUnitMessage,
                            keyboardType = KeyboardType.Number,
                            leadingIcon = {
                                Text(
                                    text = stringResource(R.string.price),
                                    fontWeight = FontWeight.Bold
                                )
                            },
                        )
                    }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(
                                16.dp,
                                Alignment.CenterHorizontally
                            )
                        ) {
                            if (state.id != null) {
                                OutlinedButton(
                                    modifier = Modifier.weight(1f),
                                    contentPadding = PaddingValues(vertical = 12.dp),
                                    border = BorderStroke(
                                        2.dp,
                                        MaterialTheme.colorScheme.errorContainer
                                    ),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.errorContainer,
                                    ),
                                    onClick = {
                                        onEvent(FormEvent.OnDeleteClicked(!state.isDeleting))
                                    }
                                ) {
                                    Text(text = stringResource(R.string.delete))
                                }
                            }
                            Button(
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    onEvent(FormEvent.OnSave)
                                },
                                contentPadding = PaddingValues(vertical = 12.dp),
                                shape = RoundedCornerShape(16.dp),
                                enabled = state.nameMessage.isBlank() &&
                                        state.unitMessage.isBlank() &&
                                        state.pricePerUnitMessage.isBlank()
                            ) {
                                Text(text = stringResource(R.string.create_label))
                            }
                        }
                    }
                }
            }
        }
    )

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