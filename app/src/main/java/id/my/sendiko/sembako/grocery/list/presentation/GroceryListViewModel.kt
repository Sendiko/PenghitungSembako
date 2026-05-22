package id.my.sendiko.sembako.grocery.list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.my.sendiko.sembako.core.preferences.UiMode
import id.my.sendiko.sembako.grocery.core.domain.Grocery
import id.my.sendiko.sembako.grocery.list.data.GroceryListRepositoryImpl
import id.my.sendiko.sembako.grocery.list.data.dto.SaveTransactionRequest
import id.my.sendiko.sembako.store.core.domain.Store
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GroceryListViewModel(
    val repository: GroceryListRepositoryImpl
) : ViewModel() {

    private val _uiMode = repository.getUiMode()
    private val _user = repository.getUser()
    private val _state = MutableStateFlow(GroceryListState())
    val state = combine(_user, _uiMode, _state) { user, uiMode, state ->
        state.copy(user = user, uiMode = uiMode)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GroceryListState())

    fun onEvent(event: GroceryListEvent) {
        when (event) {
            is GroceryListEvent.OnQuantityChange -> changeQuantity(event.quantity)
            is GroceryListEvent.OnGroceryChange -> onSembakoClick(event.sembako)
            GroceryListEvent.OnCalculateClick -> onCalculateClick()
            GroceryListEvent.OnDismiss -> dismissBottomSheet()
            is GroceryListEvent.OnUnitChange -> changeUnit(event.unit)
            is GroceryListEvent.SetPreference -> setPreference(event.uiMode)
            GroceryListEvent.ClearState -> clearState()
            GroceryListEvent.LoadData -> loadData()
            GroceryListEvent.OnSaveTransaction -> saveTransaction()
            is GroceryListEvent.OnStoreChange -> onStoreChange(event.store)
        }
    }

    private fun onStoreChange(store: Store) {
        _state.update { it.copy(selectedStore = store, isLoading = true) }
        viewModelScope.launch {
            repository.getRemoteGroceries(store.id.toString())
                .onSuccess { groceries ->
                    repository.saveGroceries(groceries)
                    _state.update { it.copy(groceries = groceries, isLoading = false) }
                }
                .onFailure {
                    repository.getLocalGroceries().collect { localGroceries ->
                        _state.update { it.copy(groceries = localGroceries, isLoading = false) }
                    }
                }
        }
    }

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

    private fun saveTransaction() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            delay(1000)
            val request = SaveTransactionRequest(
                quantity = state.value.quantity,
                totalPrice = state.value.totalPrice.toInt(),
                userId = state.value.user!!.id,
                groceryId = state.value.grocery!!.id,
                storeId = state.value.selectedStore?.id ?: 0
            )
            repository.saveTransaction(request)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            message = "Transaksi berhasil disimpan"
                        )
                    }
                }
                .onFailure {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            message = "Transaksi gagal disimpan"
                        )
                    }
                }
        }
    }

    private fun clearState() {
        dismissBottomSheet()
        _state.update { it.copy(groceries = emptyList(), message = "") }
    }

    private fun loadData() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            repository
                .getStores(state.value.user?.id ?: 0)
                .onSuccess { result ->
                    val selectedStore = result.firstOrNull()
                    _state.update {
                        it.copy(stores = result, selectedStore = selectedStore)
                    }
                    if (selectedStore != null) {
                        repository
                            .getRemoteGroceries(selectedStore.id.toString())
                            .onSuccess { groceries ->
                                repository.saveGroceries(groceries)
                                _state.update {
                                    it.copy(groceries = groceries, isLoading = false)
                                }
                            }
                            .onFailure {
                                repository
                                    .getLocalGroceries()
                                    .collect { localGroceries ->
                                        _state.update {
                                            it.copy(groceries = localGroceries, isLoading = false)
                                        }
                                    }
                            }
                    } else {
                        _state.update { it.copy(isLoading = false) }
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isError = true,
                            message = error.message ?: "Error getting your store."
                        )
                    }
                }
        }
    }

    private fun setPreference(uiMode: UiMode) = viewModelScope.launch {
        repository.setUiMode(uiMode)
    }
}