package com.github.sendiko.penghitungsembako.login.presentation

import androidx.credentials.GetCredentialResponse

sealed interface LoginEvent {
    data object OnLoginClicked: LoginEvent
    data class OnResult(val result: GetCredentialResponse): LoginEvent
    data object ClearState: LoginEvent
}