package com.github.sendiko.penghitungsembako.user.presentation

import android.app.Application
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.sendiko.penghitungsembako.core.preferences.UserPreferences
import com.github.sendiko.penghitungsembako.core.preferences.dataStore
import com.github.sendiko.penghitungsembako.user.domain.User
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    app: Application
) : AndroidViewModel(app) {

    private val _prefs = UserPreferences(app.applicationContext.dataStore)
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
                val user = User(
                    username = googleId.displayName ?: "",
                    email = googleId.id,
                    profileUrl = googleId.profilePictureUri.toString()
                )
                viewModelScope.launch { _prefs.saveUser(user) }
                _state.update { it.copy(isSignInSuccessful = true) }
            } catch (e: GoogleIdTokenParsingException) {
                e.printStackTrace()
                _state.update { it.copy(signInError = e.message.toString()) }
            }
        } else {
            _state.update { it.copy(signInError = "Invalid credential type") }
        }
    }
}