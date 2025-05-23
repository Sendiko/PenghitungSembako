package com.github.sendiko.penghitungsembako.user.presentation

import androidx.credentials.GetCredentialResponse

sealed interface LoginEvent {
    data object OnLoginClicked: LoginEvent
    data class OnResult(val result: GetCredentialResponse): LoginEvent
    data object ClearState: LoginEvent
}