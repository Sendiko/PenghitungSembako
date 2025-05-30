package com.github.sendiko.penghitungsembako.grocery.form.presentation

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sendiko.penghitungsembako.grocery.form.data.FormRepositoryImpl
import com.github.sendiko.penghitungsembako.grocery.form.data.dto.PostGroceryRequest
import com.github.sendiko.penghitungsembako.grocery.form.data.dto.UpdateGroceryRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class FormViewModel(
    private val repository: FormRepositoryImpl
) : ViewModel() {

    private val _user = repository.getUser()
    private val _state = MutableStateFlow(FormState())
    val state = combine(_user, _state) { user, state ->
        state.copy(user = user)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FormState())

    fun onEvent(event: FormEvent) {
        when (event) {
            is FormEvent.OnNameChanged -> updateName(event.name)
            is FormEvent.OnPricePerUnitChanged -> updatePricePerUnit(event.pricePerUnit)
            is FormEvent.OnUnitChanged -> updateUnit(event.unit)
            is FormEvent.OnDeleteClicked -> updateDeleting(event.isDeleting)
            FormEvent.OnSave -> saveGrocery()
            FormEvent.OnDelete -> deleteGrocery()
            is FormEvent.OnDropDownChanged -> updateDropdown(event.isExpanding)
            is FormEvent.OnImageChosen -> handleImage(event.bitmap)
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val steam = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.PNG, 100, steam)
        val byteArray = steam.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/png".toMediaTypeOrNull(),
            0,
            byteArray.size
        )
        return MultipartBody.Part.createFormData("image", state.value.name.snakeCase(), requestBody)
    }

    private fun String.snakeCase(): String {
        if (this.isEmpty())
            return "_"
        var string = this

        string.replace("-", "_").replace(" ", "_")
        string = Regex("(?<=[a-z0-9])([A-Z])").replace(string, "_$1")
        string = Regex("(?<=[A-Z])([A-Z])(?=[a-z])").replace(string, "_$1")
        string = string.lowercase()

        string = Regex("_+").replace(string, "_")
        string = string.trim('_')
        return string
    }

    private fun handleImage(bitmap: Bitmap?) {
        _state.update { it.copy(bitmap = bitmap) }
    }

    private fun updateDropdown(isExpanding: Boolean) {
        _state.update { it.copy(isExpanding = isExpanding) }
    }

    private fun updateDeleting(isDeleting: Boolean) {
        _state.update { it.copy(isDeleting = isDeleting) }
    }

    private fun deleteGrocery() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        state.value.id?.let { id ->
            repository.deleteGroceryFromRemote(id)
                .onSuccess { result ->
                    when (result) {
                        200 -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    message = "Grocery successfully deleted.",
                                    isDeleted = true
                                )
                            }
                        }

                        404 -> _state.update {
                            it.copy(
                                isLoading = false,
                                message = "Not Found."
                            )
                        }

                        else -> _state.update {
                            it.copy(
                                isLoading = false,
                                message = "Server Error."
                            )
                        }
                    }
                }
        }
    }

    fun setId(id: Int?) {
        _state.update { it.copy(id = id) }
        if (id != null) {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                delay(1000)
                repository.getGroceryFromRemote(state.value.id!!.toInt())
                    .onSuccess { result ->
                        _state.update {
                            it.copy(
                                id = result.id,
                                name = result.name,
                                pricePerUnit = result.pricePerUnit.toString(),
                                unit = result.unit,
                                isLoading = false,
                                imageUrl = result.imageUrl
                            )
                        }
                    }
                    .onFailure { exception ->
                        _state.update { it.copy(isLoading = false, message = exception.message.toString()) }
                    }
            }
        }
    }

    private fun updateName(name: String) {
        _state.update { it.copy(name = name) }
    }

    private fun updatePricePerUnit(pricePerUnit: String) {
        _state.update { it.copy(pricePerUnit = pricePerUnit) }
    }

    private fun updateUnit(unit: String) {
        _state.update { it.copy(unit = unit) }
    }

    private fun saveGrocery() {
        if (state.value.name.isBlank()) {
            _state.update {
                it.copy(
                    nameMessage = "Nama tidak boleh kosong"
                )
            }
            return
        }
        if (state.value.unit.isBlank()) {
            _state.update {
                it.copy(
                    unitMessage = "Satuan tidak boleh kosong"
                )
            }
            return
        }
        if (state.value.pricePerUnit.isBlank()) {
            _state.update {
                it.copy(
                    pricePerUnitMessage = "Harga tidak boleh kosong"
                )
            }
            return
        }
        _state.update { it.copy(isLoading = true) }
        parseCurrencyString(state.value.pricePerUnit)?.let {
            viewModelScope.launch {
                if (state.value.id != null) {
                    val request = UpdateGroceryRequest(
                        userId = state.value.user.id,
                        name = state.value.name,
                        unit = state.value.unit,
                        pricePerUnit = it,
                        image = if (state.value.bitmap != null)
                            state.value.bitmap!!.toMultipartBody()
                        else null
                    )
                    repository.updateGroceryToRemote(requireNotNull(state.value.id), request)
                        .onSuccess { result ->
                            when (result) {
                                200 -> {
                                    _state.update {
                                        it.copy(
                                            isLoading = false,
                                            message = "Grocery successfully updated.",
                                            isSaved = true
                                        )
                                    }
                                }

                                400 -> _state.update {
                                    it.copy(
                                        isLoading = false,
                                        message = "Bad Request."
                                    )
                                }

                                else -> _state.update {
                                    it.copy(
                                        isLoading = false,
                                        message = "Server Error."
                                    )
                                }
                            }
                        }
                } else {
                    val request = PostGroceryRequest(
                        userId = state.value.user.id,
                        name = state.value.name,
                        unit = state.value.unit,
                        pricePerUnit = it,
                        image = state.value.bitmap!!.toMultipartBody()
                    )
                    repository.saveGroceryToRemote(request)
                        .onSuccess { result ->
                            when (result) {
                                201 -> _state.update {
                                    it.copy(
                                        isLoading = false,
                                        message = "Grocery successfully stored.",
                                        isSaved = true
                                    )
                                }

                                400 -> _state.update {
                                    it.copy(
                                        isLoading = false,
                                        message = "Bad Request."
                                    )
                                }

                                else -> _state.update {
                                    it.copy(
                                        isLoading = false,
                                        message = "Server Error."
                                    )
                                }
                            }
                        }
                        .onFailure { exception ->
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    message = exception.message.toString()
                                )
                            }
                        }
                }
            }
            return
        }
        _state.update {
            it.copy(message = "Harga harus berupa angka")
        }
    }

    private fun parseCurrencyString(input: String): Double? {
        return try {
            val cleaned = input
                .replace(".", "")
            cleaned.toDouble()
        } catch (_: NumberFormatException) {
            null
        }
    }


}