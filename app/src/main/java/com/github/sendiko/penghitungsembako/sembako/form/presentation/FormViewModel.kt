package com.github.sendiko.penghitungsembako.sembako.form.presentation

import android.R.attr.name
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sendiko.penghitungsembako.sembako.core.data.Sembako
import com.github.sendiko.penghitungsembako.sembako.core.data.SembakoDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FormViewModel(
    private val dao: SembakoDao
) : ViewModel() {

    private val _state = MutableStateFlow(FormState())
    val state = _state.asStateFlow()

    fun onEvent(event: FormEvent) {
        when (event) {
            is FormEvent.OnNameChanged -> updateName(event.name)
            is FormEvent.OnPricePerUnitChanged -> updatePricePerUnit(event.pricePerUnit)
            is FormEvent.OnUnitChanged -> updateUnit(event.unit)
            FormEvent.OnSave -> saveSembako()
            FormEvent.OnDelete -> deleteSembako()
        }
    }

    private fun deleteSembako() = viewModelScope.launch {
        state.value.id?.let { id ->
            dao.delete(id)
            _state.update { it.copy(isDeleted = true) }
        }
    }

    fun setId(id: Int?) {
        _state.update { it.copy(id = id) }
        if (id != null) {
            viewModelScope.launch {
                val sembako = dao.getById(id)
                _state.update {
                    it.copy(
                        name = sembako.name,
                        pricePerUnit = sembako.pricePerUnit.toString(),
                        unit = sembako.unit
                    )
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

    private fun saveSembako() {
        if (state.value.name.isBlank()) {
            _state.update {
                it.copy(
                    message = "Nama tidak boleh kosong"
                )
            }
            return
        }
        if (state.value.unit.isBlank()) {
            _state.update {
                it.copy(
                    message = "Satuan tidak boleh kosong"
                )
            }
            return
        }
        if (state.value.pricePerUnit.isBlank()) {
            _state.update {
                it.copy(
                    message = "Harga tidak boleh kosong"
                )
            }
            return
        }
        parseCurrencyString(state.value.pricePerUnit)?.let {
            val sembako = Sembako(
                id = state.value.id?:0,
                name = state.value.name,
                pricePerUnit = it,
                unit = state.value.unit
            )
            viewModelScope.launch {
                if (state.value.id != null)
                    dao.update(sembako)
                else
                    dao.insert(sembako)
            }
            _state.update { it.copy(isSaved = true) }
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
        } catch (e: NumberFormatException) {
            null
        }
    }


}