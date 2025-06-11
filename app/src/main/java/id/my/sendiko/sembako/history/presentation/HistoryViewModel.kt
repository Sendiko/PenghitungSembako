package id.my.sendiko.sembako.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.my.sendiko.sembako.history.data.HistoryRepositoryImpl
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
        }
    }

    private fun loadData() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            delay(1000)
            repository.getHistory(state.value.user?.id.toString())
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            histories = result
                        )
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