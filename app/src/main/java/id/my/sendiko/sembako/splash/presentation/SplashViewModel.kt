package id.my.sendiko.sembako.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.my.sendiko.sembako.splash.data.SplashRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class SplashViewModel(
    repo: SplashRepositoryImpl
): ViewModel() {

    private val _user = repo.getUser()
    private val _state = MutableStateFlow(SplashState())
    val state = combine(_user, _state) { user, state ->
        state.copy(user = user)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SplashState())

}