package id.my.sendiko.sembako.dashboard.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.my.sendiko.sembako.dashboard.data.DashboardRepositoryImpl
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    repository: DashboardRepositoryImpl
) : ViewModel() {

    private val _user = repository.getUser()
    private val _state = MutableStateFlow(DashboardState())
    val state = combine(_user, _state) { user, state ->
        Log.i("User", "state: $user")
        state.copy(user = user)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardState())

    private val _signInEvent = MutableSharedFlow<Unit>()
    val signInEvent = _signInEvent.asSharedFlow()

    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.OnLoginClicked -> {
                _state.update { it.copy(isSigningIn = true) }
                viewModelScope.launch {
                    _signInEvent.emit(Unit)
                }
            }
            DashboardEvent.ClearState -> _state.update {
                it.copy(signInError = "", isSigningIn = false, message = "")
            }
        }
    }
}