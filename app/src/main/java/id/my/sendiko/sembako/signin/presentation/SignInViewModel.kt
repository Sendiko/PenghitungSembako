package id.my.sendiko.sembako.signin.presentation

import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import id.my.sendiko.sembako.core.domain.User
import id.my.sendiko.sembako.signin.domain.SignInRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel(private val repository: SignInRepository) : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.OnGoogleSignedIn -> signInWithGoogle(event.response)
            SignInEvent.OnGuestSignedIn -> signInAsGuest()
            SignInEvent.OnClearState -> clearState()
        }
    }

    private fun clearState() {
        _state.update {
            it.copy(
                isSuccess = false,
                isError = false,
                isLoading = false,
                message = ""
            )
        }
    }

    private fun signInAsGuest() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.signInAnonymously()
                .onSuccess { result ->
                    val user = User(
                        id = result?.uid,
                        username = result?.displayName,
                        email = result?.email,
                        profileUrl = result?.photoUrl
                    )
                    repository.saveUserToRemote(user)
                        .onSuccess {
                            repository.saveUserToLocal(user)
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    message = "You're logged in as Guest.",
                                    isSuccess = true
                                )
                            }
                        }
                        .onFailure { error ->
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    isError = true,
                                    message = "Failed to sign in: ${error.localizedMessage}"
                                )
                            }
                        }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            message = "Failed to sign in: ${error.localizedMessage}"
                        )
                    }
                }
        }
    }

    private fun signInWithGoogle(response: Result<GetCredentialResponse>) {
        viewModelScope.launch {
            response.onSuccess { result ->
                val credential = result.credential
                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleId = GoogleIdTokenCredential.createFrom(credential.data)
                    val user = User(
                        username = googleId.displayName ?: "No Name",
                        email = googleId.id,
                        profileUrl = googleId.profilePictureUri,
                        id = ""
                    )
                    repository.signInWithGoogle(googleId.idToken)
                        .onSuccess {
                            repository.saveUserToRemote(user)
                                .onSuccess {
                                    repository.saveUserToLocal(user)
                                    _state.update {
                                        it.copy(
                                            isLoading = false,
                                            message = "You're logged in as ${user.username}.",
                                            isSuccess = true
                                        )
                                    }
                                }
                        }
                }
            }
        }
    }

}