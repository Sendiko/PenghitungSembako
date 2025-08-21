package id.my.sendiko.sembako.signin.presentation

import androidx.credentials.GetCredentialResponse

sealed interface SignInEvent {
    data class OnGoogleSignedIn(val response: Result<GetCredentialResponse>): SignInEvent
    data object OnGuestSignedIn: SignInEvent
    data object OnClearState: SignInEvent
}