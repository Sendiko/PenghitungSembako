package id.my.sendiko.sembako.dashboard.presentation

import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import id.my.sendiko.sembako.core.domain.User
import id.my.sendiko.sembako.dashboard.data.DashboardRepositoryImpl
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: DashboardRepositoryImpl
) : ViewModel() {

    private val _user = repository.getUser()
    private val _state = MutableStateFlow(DashboardState())
    val state = combine(_user, _state) { user, state ->
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

            is DashboardEvent.OnResult -> handleSignInResult(event.result)

        }
    }

    private fun handleSignInResult(result: Result<GetCredentialResponse>) {
        result.onSuccess { response ->
            val credential = response.credential
            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                try {
                    val googleId = GoogleIdTokenCredential.createFrom(credential.data)
                    val user = User(
                        username = googleId.displayName ?: "No Name",
                        email = googleId.id,
                        profileUrl = googleId.profilePictureUri.toString(),
                        id = 0
                    )
                    viewModelScope.launch {
                        repository.saveUserToRemote(user)
                            .onSuccess { remoteUser ->
                                repository.saveUserToLocal(remoteUser)
                                _state.update { it.copy(
                                    isSignInSuccessful = true,
                                    isSigningIn = false,
                                    message = "Berhasil login",
                                    user = remoteUser
                                ) }
                            }
                            .onFailure { remoteError ->
                                _state.update { it.copy(
                                    isSigningIn = false,
                                    signInError = remoteError.message ?: "Failed to save user to remote."
                                ) }
                            }
                    }
                } catch (e: GoogleIdTokenParsingException) {
                    e.printStackTrace()
                    _state.update { it.copy(signInError = "Gagal mem-parsing token: ${e.message}", isSigningIn = false) }
                }
            } else {
                _state.update { it.copy(signInError = "Invalid credential type received.", isSigningIn = false) }
            }
        }.onFailure { exception ->
            val errorMessage = if (exception is ApiException) {
                "Login dibatalkan atau gagal: ${exception.statusCode}"
            } else {
                exception.message ?: "Terjadi kesalahan"
            }
            _state.update { it.copy(signInError = errorMessage, isSigningIn = false) }
        }
    }
}