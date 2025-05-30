package com.github.sendiko.penghitungsembako.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sendiko.penghitungsembako.profile.data.ProfileRepositoryImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repo: ProfileRepositoryImpl
) : ViewModel() {

    private val _dynamicTheme = repo.getDynamicTheme()
    private val _user = repo.getUser()
    private val _state = MutableStateFlow(ProfileState())
    val state = combine(_user, _dynamicTheme, _state) { user, dynamicTheme, state ->
        state.copy(user = user, dynamicTheme = dynamicTheme)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProfileState())

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.OnLogoutClicked -> viewModelScope.launch {
                _state.update { it.copy(isSigningOut = true) }
                repo.logout()
                    .onSuccess {
                        _state.update {
                            it.copy(
                                isSigningOut = false,
                                isSignOutSuccessful = true,
                            )
                        }
                    }
                    .onFailure {
                        _state.update {
                            it.copy(
                                isSigningOut = false,
                                signOutError = it.signOutError,
                                errorMessage = "Failed to logout."
                            )
                        }
                    }
            }

            ProfileEvent.ClearState -> _state.update {
                it.copy(
                    isSigningOut = false,
                    isSignOutSuccessful = false,
                    signOutError = "",
                )
            }

            is ProfileEvent.OnThemeChanged -> viewModelScope.launch {
                repo.setDynamicTheme(event.dynamicTheme)
            }
        }
    }
}