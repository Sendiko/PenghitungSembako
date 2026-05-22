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

class StoreViewModel(
    private val repository: StoreRepository
) : ViewModel() {

    private val _user = repository.getUser()
    private val _state = MutableStateFlow(StoreState())
    val state = combine(_state, _user) { state, user ->
        state.copy(user = user)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StoreState())

    fun onEvent(event: StoreEvent) {
        when (event) {
            StoreEvent.OnLoadData -> loadData()
            StoreEvent.OnClearState -> clearState()
        }
    }

    private fun clearState() {
        _state.update { it.copy(
            isLoading = false,
            isError = false,
            message = ""
        ) }
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