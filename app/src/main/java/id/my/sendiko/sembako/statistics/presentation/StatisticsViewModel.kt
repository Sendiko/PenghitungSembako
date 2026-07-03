package id.my.sendiko.sembako.statistics.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.my.sendiko.sembako.statistics.data.StatisticsRepositoryImpl
import id.my.sendiko.sembako.statistics.domain.Statistics
import id.my.sendiko.sembako.store.core.domain.Store
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class StatisticsViewModel(
    private val repository: StatisticsRepositoryImpl
) : ViewModel() {

    private val _user = repository.getUser()
    private val _state = MutableStateFlow(StatisticsState())
    val state = combine(_user, _state) { user, state ->
        state.copy(user = user)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StatisticsState())

    fun onEvent(event: StatisticsEvent) {
        when (event) {
            is StatisticsEvent.LoadData -> loadData()
            StatisticsEvent.ClearState -> clearState()
            is StatisticsEvent.OnStoreChange -> onStoreChange(event.store)
        }
    }

    private fun clearState() {
        _state.update { it.copy(message = "") }
    }

    private fun onStoreChange(store: Store) {
        _state.update { it.copy(selectedStore = store, isLoading = true) }
        viewModelScope.launch {
            repository.getStatisticsFromRemote(store.id.toString())
                .onSuccess { result ->
                    val statistics = Statistics.fromStatisticsItem(result)
                    repository.saveStatisticsToLocal(statistics = statistics)
                    _state.update { it.copy(statistics = statistics, isLoading = false) }
                }
                .onFailure {
                    repository.getStatisticsFromLocal().collect { statistics ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                message = "Server error.",
                                statistics = statistics
                            )
                        }
                    }
                }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            delay(1.seconds)
            _state.update { it.copy(isLoading = true) }
            repository.getStores(state.value.user?.id ?: 0)
                .onSuccess { result ->
                    val selectedStore = result.firstOrNull()
                    _state.update {
                        it.copy(stores = result, selectedStore = selectedStore)
                    }
                    if (selectedStore != null) {
                        repository.getStatisticsFromRemote(state.value.user?.id.toString())
                            .onSuccess { result ->
                                val statistics = Statistics.fromStatisticsItem(result)
                                repository.saveStatisticsToLocal(statistics = statistics)
                                _state.update {
                                    it.copy(
                                        statistics = statistics,
                                        isLoading = false
                                    )
                                }
                            }
                            .onFailure { error ->
                                repository.getStatisticsFromLocal().collect { statistics ->
                                    _state.update {
                                        it.copy(
                                            isLoading = false,
                                            message = error.localizedMessage ?: "Server error.",
                                            statistics = statistics
                                        )
                                    }
                                }
                            }
                    } else {
                        _state.update { it.copy(isLoading = false) }
                    }
                }
        }

    }

}