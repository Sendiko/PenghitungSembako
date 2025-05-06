package com.github.sendiko.penghitungsembako.sembako.dashboard.presentation

import androidx.lifecycle.ViewModel
import com.github.sendiko.penghitungsembako.sembako.core.data.Sembako
import com.github.sendiko.penghitungsembako.sembako.core.data.SembakoDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DashboardViewModel(private val dao: SembakoDao) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    fun dismissBottomSheet() {
        _state.update {
            it.copy(
                selectedSembako = null,
                quantity = "",
                totalPrice = 0.0
            )
        }
    }

    fun onCalculateClick() {
        if (state.value.quantity.toIntOrNull() == null) {
            _state.update {
                it.copy(message = "Masukkan jumlah barang yang valid")
            }
            return
        }
        if (state.value.quantity.toInt() < 0) {
            _state.update {
                it.copy(message = "Masukkan jumlah barang yang valid")
            }
            return
        }
        if (state.value.quantity.isBlank()) {
            _state.update {
                it.copy(message = "Masukkan jumlah barang yang ingin dibeli")
            }
        }
        if (state.value.quantity.isNotBlank()) {
            if (!state.value.usingOns) {
                _state.update {
                    it.copy(
                        totalPrice = it.selectedSembako?.pricePerUnit?.times(it.quantity.toDouble())
                            ?: 0.0
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        totalPrice = it.selectedSembako?.pricePerUnit?.times(it.quantity.toDouble())?.div(10)
                            ?: 0.0
                    )
                }
            }
            return
        }
    }

    fun onSembakoClick(sembako: Sembako) {
        _state.update {
            it.copy(selectedSembako = sembako)
        }
    }

    fun changeQuantity(quantity: String) {
        _state.update { it.copy(quantity = quantity) }
    }

    private fun changeUnit(usingOns: Boolean) {
        _state.update { it.copy(usingOns = usingOns) }
    }

    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.OnQuantityChange -> changeQuantity(event.quantity)
            is DashboardEvent.OnSembakoClick -> onSembakoClick(event.sembako)
            DashboardEvent.OnCalculateClick -> onCalculateClick()
            DashboardEvent.OnDismiss -> dismissBottomSheet()
            is DashboardEvent.OnUnitChange -> changeUnit(event.unit)
        }
    }
}