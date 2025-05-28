package com.github.sendiko.penghitungsembako.login.presentation

import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sendiko.penghitungsembako.core.domain.User
import com.github.sendiko.penghitungsembako.login.domain.LoginRepository
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    val repository: LoginRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnLoginClicked -> _state.update { it.copy(isSigningIn = true) }
            is LoginEvent.OnResult -> viewModelScope.launch {
                handleSignIn(event.result)
            }

            LoginEvent.ClearState -> _state.update {
                it.copy(signInError = "", isSigningIn = false)
            }

        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        val credential = result.credential
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                val googleId = GoogleIdTokenCredential.createFrom(credential.data)
                var user = User(
                    username = googleId.displayName ?: "",
                    email = googleId.id,
                    profileUrl = googleId.profilePictureUri.toString(),
                    id = 0
                )
                viewModelScope.launch {
                    repository.saveUserToRemote(user)
                        .onSuccess { result ->
                            repository.saveUserToLocal(result)
                                .onSuccess {
                                    _state.update { it.copy(isSignInSuccessful = true) }
                                }
                                .onFailure {
                                    val clearUser = User(0, "", "", "")
                                    repository.saveUserToLocal(clearUser)
                                }
                        }
                }
            } catch (e: GoogleIdTokenParsingException) {
                e.printStackTrace()
                _state.update { it.copy(signInError = e.message.toString()) }
            }
        } else {
            _state.update { it.copy(signInError = "Invalid credential type") }
        }
    }
}