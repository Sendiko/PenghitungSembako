package id.my.sendiko.sembako.signin.presentation

sealed interface SignInEvent {
    data object OnSignIn: SignInEvent
}