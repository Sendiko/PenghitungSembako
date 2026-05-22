package id.my.sendiko.sembako.dashboard.presentation

import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import id.my.sendiko.sembako.dashboard.domain.DashboardRepository
import id.my.sendiko.sembako.store.core.domain.Store
import id.my.sendiko.sembako.user.core.domain.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: DashboardRepository
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
            is DashboardEvent.OnLoginClicked -> onLoginClicked()
            DashboardEvent.ClearState -> clearState()
            is DashboardEvent.OnResult -> handleSignInResult(event.result)
            is DashboardEvent.OnStoreAddressChange -> updateStoreAddress(event.address)
            is DashboardEvent.OnStoreEmailChange -> updateStoreEmail(event.email)
            is DashboardEvent.OnStoreNameChange -> updateStoreName(event.name)
            is DashboardEvent.OnStorePhoneChange -> updateStorePhone(event.phone)
            is DashboardEvent.OnStoreSheetVisible -> updateStoreSheetVisible(event.isVisible)
            DashboardEvent.OnSaveStore -> onSaveStore()
        }
    }

    private fun onLoginClicked() {
        _state.update { it.copy(isSigningIn = true) }
        viewModelScope.launch {
            _signInEvent.emit(Unit)
        }
    }

    private fun clearState() {
        _state.update {
            it.copy(signInError = "", isSigningIn = false, message = "")
        }
    }

    private fun updateStoreAddress(address: String) {
        _state.update { it.copy(storeAddress = address) }
    }

    private fun updateStoreEmail(email: String) {
        _state.update { it.copy(storeEmail = email) }
    }

    private fun updateStoreName(name: String) {
        _state.update { it.copy(storeName = name) }
    }

    private fun updateStorePhone(phone: String) {
        _state.update { it.copy(storePhone = phone) }
    }

    private fun updateStoreSheetVisible(isVisible: Boolean) {
        _state.update { it.copy(isStoreSheetVisible = isVisible) }
    }

    private fun onSaveStore() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            repository.saveStore(
                Store(
                    id = 0,
                    name = state.value.storeName,
                    address = state.value.storeAddress,
                    phone = state.value.storePhone,
                    email = state.value.storeEmail
                )
            )
                .onSuccess { result ->
                    repository.setHasStore()
                    _state.update {
                        it.copy(
                            isLoading = false,
                            message = "Toko ${result.name} telah terdaftar!"
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            message = error.message ?: "Terjadi kesalahan.",
                            isError = true
                        )
                    }
                }
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
                        id = 0,
                        hasStore = false
                    )
                    viewModelScope.launch {
                        repository.saveUserToRemote(user)
                            .onSuccess { remoteUser ->
                                repository.saveUserToLocal(remoteUser)
                                _state.update {
                                    it.copy(
                                        isSignInSuccessful = true,
                                        isSigningIn = false,
                                        message = "Berhasil login",
                                        user = remoteUser
                                    )
                                }
                            }
                            .onFailure { remoteError ->
                                _state.update {
                                    it.copy(
                                        isSigningIn = false,
                                        signInError = remoteError.message
                                            ?: "Failed to save user to remote.",
                                        isError = true
                                    )
                                }
                            }
                    }
                } catch (e: GoogleIdTokenParsingException) {
                    e.printStackTrace()
                    _state.update {
                        it.copy(
                            signInError = "Gagal mem-parsing token: ${e.message}",
                            isSigningIn = false
                        )
                    }
                }
            } else {
                _state.update {
                    it.copy(
                        signInError = "Invalid credential type received.",
                        isSigningIn = false
                    )
                }
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
