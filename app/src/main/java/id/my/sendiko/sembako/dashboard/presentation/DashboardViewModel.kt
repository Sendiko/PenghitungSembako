package id.my.sendiko.sembako.dashboard.presentation

import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import id.my.sendiko.sembako.core.domain.User
import id.my.sendiko.sembako.dashboard.data.DashboardRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: DashboardRepositoryImpl
) : ViewModel() {

    private val _user = repository.getUser()
    private val _state = MutableStateFlow(DashboardState())
    val state = combine(_user, _state) { user, state ->
        state.copy(user = user)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardState())

    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.OnLoginClicked -> _state.update { it.copy(isSigningIn = true) }
            is DashboardEvent.OnResult -> viewModelScope.launch {
                handleSignIn(event.result)
            }
            DashboardEvent.ClearState -> _state.update {
                it.copy(signInError = "", isSigningIn = false, message = "")
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
                    profileUrl = googleId.profilePictureUri.toString(),
                    id = 0
                )
                viewModelScope.launch {
                    repository.saveUserToRemote(user)
                        .onSuccess { result ->
                            repository.saveUserToLocal(result)
                                .onSuccess {
                                    _state.update { it.copy(
                                        isSignInSuccessful = true,
                                        message = "Berhasil login",
                                        user = result
                                    ) }
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