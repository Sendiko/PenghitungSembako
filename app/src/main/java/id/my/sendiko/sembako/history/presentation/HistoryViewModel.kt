package id.my.sendiko.sembako.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.my.sendiko.sembako.history.data.HistoryRepositoryImpl
import id.my.sendiko.sembako.store.core.domain.Store
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: HistoryRepositoryImpl
) : ViewModel() {

    private val _user = repository.getUser()
    private val _state = MutableStateFlow(HistoryState())
    val state = combine(_user, _state) { user, state ->
        state.copy(user = user)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HistoryState())

    fun onEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.LoadData -> loadData()
            HistoryEvent.ClearState -> clearState()
            is HistoryEvent.OnStoreChange -> onStoreChange(event.store)
        }
    }

    private fun onStoreChange(store: Store) {
        _state.update { it.copy(selectedStore = store, isLoading = true) }
        viewModelScope.launch {
            repository.getRemoteHistories(store.id.toString())
                .onSuccess { histories ->
                    repository.saveHistoriesToLocal(histories)
                    _state.update { it.copy(histories = histories, isLoading = false) }
                }
                .onFailure { throwable ->
                    repository.getLocalHistories().collect { resultFromLocal ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                message = throwable.message.toString(),
                                histories = resultFromLocal
                            )
                        }
                    }
                }
        }
    }

    private fun clearState() {
        _state.update { it.copy(message = "")}
    }

    private fun loadData() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            repository.getStores(state.value.user?.id ?: 0)
                .onSuccess { result ->
                    val selectedStore = result.firstOrNull()
                    _state.update {
                        it.copy(stores = result, selectedStore = selectedStore)
                    }
                    if (selectedStore != null) {
                        repository.getRemoteHistories(selectedStore.id.toString())
                            .onSuccess { histories ->
                                repository.saveHistoriesToLocal(histories)
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        histories = histories
                                    )
                                }
                            }
                            .onFailure { throwable ->
                                repository.getLocalHistories().collect { resultFromLocal ->
                                    _state.update {
                                        it.copy(
                                            isLoading = false,
                                            message = throwable.message.toString(),
                                            histories = resultFromLocal
                                        )
                                    }
                                }
                            }
                    } else {
                        _state.update { it.copy(isLoading = false) }
                    }
                }
                .onFailure { throwable ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            message = throwable.message.toString()
                        )
                    }
                }
        }
    }

}