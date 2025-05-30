package com.github.sendiko.penghitungsembako.statistics.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sendiko.penghitungsembako.statistics.data.StatisticsRepositoryImpl
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
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            delay(1000)
            _state.update { it.copy(isLoading = true) }
            repository.getStatistics(state.value.user?.id.toString())
                .onSuccess { result ->
                    _state.update { it.copy(statistics = result, isLoading = false) }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false, message = "Failed to load statistics.") }
                }
        }

    }

}