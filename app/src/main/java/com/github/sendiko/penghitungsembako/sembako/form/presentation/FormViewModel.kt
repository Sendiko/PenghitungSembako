package com.github.sendiko.penghitungsembako.sembako.form.presentation

import androidx.lifecycle.ViewModel
import com.github.sendiko.penghitungsembako.sembako.core.data.SembakoDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FormViewModel(
    private val dao: SembakoDao
): ViewModel() {

    private val _state = MutableStateFlow(FormState())
    val state = _state.asStateFlow()

    fun onEvent(event: FormEvent) {
        when(event) {
            is FormEvent.OnNameChanged -> updateName(event.name)
            is FormEvent.OnPricePerUnitChanged -> updatePricePerUnit(event.pricePerUnit)
            is FormEvent.OnUnitChanged -> updateUnit(event.unit)
            FormEvent.OnSave -> saveSembako()
        }
    }

    private fun updateName(name: String) {

    }

    private fun updatePricePerUnit(pricePerUnit: String) {

    }

    private fun updateUnit(unit: String) {

    }

    private fun saveSembako() {

    }

}