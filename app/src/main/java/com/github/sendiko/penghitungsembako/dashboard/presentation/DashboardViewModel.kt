package com.github.sendiko.penghitungsembako.dashboard.presentation

import androidx.lifecycle.ViewModel
import com.github.sendiko.penghitungsembako.core.data.Sembako
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DashboardViewModel: ViewModel() {

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
        if (state.value.quantity.isNotBlank()) {
            _state.update {
                it.copy(
                    totalPrice = it.selectedSembako?.pricePerUnit?.times(it.quantity.toDouble()) ?: 0.0
                )
            }
            return
        }
        if (state.value.quantity.isBlank()) {
            _state.update {
                it.copy(message = "Masukkan jumlah barang yang ingin dibeli")
            }
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

    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.OnQuantityChange -> changeQuantity(event.quantity)
            is DashboardEvent.OnSembakoClick -> onSembakoClick(event.sembako)
            DashboardEvent.OnCalculateClick -> onCalculateClick()
            DashboardEvent.OnDismiss -> dismissBottomSheet()
        }
    }
}