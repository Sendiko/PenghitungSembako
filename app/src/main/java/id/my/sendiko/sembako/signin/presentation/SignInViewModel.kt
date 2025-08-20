package id.my.sendiko.sembako.signin.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SignInViewModel: ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onEvent(event: SignInEvent) {
        when(event) {
            is SignInEvent.OnSignIn -> signIn()
        }
    }

    private fun signIn() {

    }

}