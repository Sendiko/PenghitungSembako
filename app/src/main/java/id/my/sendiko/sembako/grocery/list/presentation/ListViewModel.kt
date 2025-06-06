package id.my.sendiko.sembako.grocery.list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.my.sendiko.sembako.core.preferences.UiMode
import id.my.sendiko.sembako.grocery.core.domain.Grocery
import id.my.sendiko.sembako.grocery.list.data.ListRepositoryImpl
import id.my.sendiko.sembako.grocery.list.data.dto.SaveTransactionRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListViewModel(
    val repository: ListRepositoryImpl
) : ViewModel() {

    private val _uiMode = repository.getUiMode()
    private val _user = repository.getUser()
    private val _state = MutableStateFlow(ListState())
    val state = combine(_user, _uiMode, _state) { user, uiMode, state ->
        state.copy(user = user, uiMode = uiMode)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ListState())

    fun dismissBottomSheet() {
        _state.update {
            it.copy(
                grocery = null,
                quantity = "",
                totalPrice = 0.0
            )
        }
    }

    fun onCalculateClick() {
        if (state.value.quantity.toDoubleOrNull() == null) {
            _state.update {
                it.copy(message = "Masukkan jumlah barang yang valid")
            }
            return
        }
        if (state.value.quantity.toDouble() < 0) {
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
            _state.update {
                it.copy(
                    totalPrice = it.grocery!!.pricePerUnit.times(it.quantity.toDouble())
                )
            }
            return
        }
    }

    fun onSembakoClick(sembako: Grocery) {
        _state.update {
            it.copy(grocery = sembako)
        }
    }

    fun changeQuantity(quantity: String) {
        _state.update { it.copy(quantity = quantity) }
    }

    private fun changeUnit(usingOns: Boolean) {
        _state.update { it.copy(usingOns = usingOns) }
    }

    fun onEvent(event: ListEvent) {
        when (event) {
            is ListEvent.OnQuantityChange -> changeQuantity(event.quantity)
            is ListEvent.OnGroceryChange -> onSembakoClick(event.sembako)
            ListEvent.OnCalculateClick -> onCalculateClick()
            ListEvent.OnDismiss -> dismissBottomSheet()
            is ListEvent.OnUnitChange -> changeUnit(event.unit)
            is ListEvent.SetPreference -> setPreference(event.uiMode)
            ListEvent.ClearState -> clearState()
            ListEvent.LoadData -> loadData()
            ListEvent.OnSaveTransaction -> saveTransaction()
        }
    }

    private fun saveTransaction() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            delay(1000)
            val request = SaveTransactionRequest(
                quantity = state.value.quantity,
                totalPrice = state.value.totalPrice.toInt(),
                userId = state.value.user!!.id,
                groceryId = state.value.grocery!!.id
            )
            repository.saveTransaction(request)
                .onSuccess {
                    _state.update { it.copy(isLoading = false, message = "Transaksi berhasil disimpan") }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false, message = "Transaksi gagal disimpan") }
                }
        }
    }

    private fun clearState() {
        _state.update { it.copy(groceries = emptyList()) }
    }

    private fun loadData() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            delay(1000)
            repository
                .getRemoteGroceries(state.value.user!!.id.toString())
                .onSuccess { result ->
                    repository.saveGroceries(result)
                    _state.update { it.copy(groceries = result, isLoading = false) }
                }
                .onFailure {
                    repository.getLocalGroceries()
                        .collect { result ->
                            _state.update { it.copy(groceries = result, isLoading = false) }
                        }
                }
        }
    }

    private fun setPreference(uiMode: UiMode) = viewModelScope.launch {
        repository.setUiMode(uiMode)
    }
}