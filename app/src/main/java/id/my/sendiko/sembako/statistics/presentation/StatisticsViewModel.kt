package id.my.sendiko.sembako.statistics.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.my.sendiko.sembako.statistics.data.StatisticsRepositoryImpl
import id.my.sendiko.sembako.statistics.domain.Statistics
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StatisticsViewModel(
    private val repository: StatisticsRepositoryImpl
): ViewModel() {

    private val _user = repository.getUser()
    private val _state = MutableStateFlow(StatisticsState())
    val state = combine(_user, _state) { user, state ->
        state.copy(user = user)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StatisticsState())

    fun onEvent(event: StatisticsEvent) {
        when(event) {
            is StatisticsEvent.LoadData -> loadData()
            StatisticsEvent.ClearState -> clearState()
        }
    }

    private fun clearState() {
        _state.update { it.copy(message = "") }
    }

    private fun loadData() {
        viewModelScope.launch {
            delay(1000)
            _state.update { it.copy(isLoading = true) }
            repository.getStatisticsFromRemote(state.value.user?.id.toString())
                .onSuccess { result ->
                    val statistics = Statistics.fromStatisticsItem(result)
                    repository.saveStatisticsToLocal(statistics = statistics)
                    _state.update { it.copy(statistics = statistics, isLoading = false) }
                }
                .onFailure {
                    repository.getStatisticsFromLocal().collect { statistics ->
                        _state.update { it.copy(
                            isLoading = false,
                            message = "Can't connect to server.",
                            statistics = statistics
                        ) }
                    }
                }
        }

    }

}