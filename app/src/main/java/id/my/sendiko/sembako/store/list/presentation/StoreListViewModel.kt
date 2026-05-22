package id.my.sendiko.sembako.store.list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.my.sendiko.sembako.store.core.domain.StoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoreListViewModel(
    private val repository: StoreRepository
) : ViewModel() {

    private val _user = repository.getUser()
    private val _state = MutableStateFlow(StoreListState())
    val state = combine(_state, _user) { state, user ->
        state.copy(user = user)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StoreListState())

    fun onEvent(event: StoreListEvent) {
        when (event) {
            StoreListEvent.OnLoadData -> loadData()
            StoreListEvent.OnClearState -> clearState()
            is StoreListEvent.OnStoreAddressChange -> _state.update { it.copy(storeAddress = event.address) }
            is StoreListEvent.OnStoreEmailChange -> _state.update { it.copy(storeEmail = event.email) }
            is StoreListEvent.OnStoreNameChange -> _state.update { it.copy(storeName = event.name) }
            is StoreListEvent.OnStorePhoneChange -> _state.update { it.copy(storePhone = event.phone) }
            is StoreListEvent.OnStoreSheetVisible -> _state.update { it.copy(isStoreSheetVisible = event.isVisible) }
            StoreListEvent.OnSaveStore -> {
                if (state.value.selectedStoreId == null) {
                    saveStore()
                } else {
                    updateStore()
                }
            }

            is StoreListEvent.OnEditStore -> onEditStore(event.store)
            StoreListEvent.OnUpdateStore -> updateStore()
            StoreListEvent.OnDeleteStore -> deleteStore()
            is StoreListEvent.OnDeleteDialogOpen -> _state.update { it.copy(isDeleteDialogOpen = event.isOpen) }
        }
    }

    private fun deleteStore() {
        val storeId = state.value.selectedStoreId ?: return
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            repository.deleteStore(storeId)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isStoreSheetVisible = false,
                            message = "Toko telah dihapus!",
                            selectedStoreId = null,
                        )
                    }
                    loadData()
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            message = error.message ?: "Gagal menghapus toko.",
                            isError = true
                        )
                    }
                }
        }
    }

    private fun onEditStore(store: id.my.sendiko.sembako.store.core.domain.Store) {
        _state.update {
            it.copy(
                isStoreSheetVisible = true,
                selectedStoreId = store.id,
                storeName = store.name,
                storeAddress = store.address,
                storePhone = store.phone,
                storeEmail = store.email
            )
        }
    }

    private fun updateStore() {
        val storeId = state.value.selectedStoreId ?: return
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            repository.updateStore(
                id = storeId,
                store = id.my.sendiko.sembako.store.core.domain.Store(
                    id = storeId,
                    name = state.value.storeName,
                    address = state.value.storeAddress,
                    phone = state.value.storePhone,
                    email = state.value.storeEmail
                )
            )
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isStoreSheetVisible = false,
                            message = "Toko ${result.name} telah diubah!",
                            stores = it.stores + result
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            message = error.message ?: "Terjadi kesalahan.",
                            isError = true
                        )
                    }
                }
        }
    }

    private fun saveStore() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            repository.saveStore(
                id.my.sendiko.sembako.store.core.domain.Store(
                    id = 0,
                    name = state.value.storeName,
                    address = state.value.storeAddress,
                    phone = state.value.storePhone,
                    email = state.value.storeEmail
                )
            )
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isStoreSheetVisible = false,
                            message = "Toko ${result.name} telah terdaftar!",
                            stores = it.stores + result,
                            storeName = "",
                            storeAddress = "",
                            storeEmail = "",
                            storePhone = ""
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            message = error.message ?: "Terjadi kesalahan.",
                            isError = true
                        )
                    }
                }
        }
    }

    private fun clearState() {
        _state.update {
            it.copy(
                isLoading = false,
                isError = false,
                message = ""
            )
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.getStores(state.value.user.id)
                .onSuccess { result ->
                    _state.update { it.copy(stores = result, isLoading = false) }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isError = true,
                            message = error.message ?: "Unknown Error"
                        )
                    }
                }
        }
    }

}