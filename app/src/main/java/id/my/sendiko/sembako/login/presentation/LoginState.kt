package id.my.sendiko.sembako.login.presentation

data class LoginState(
    val isSigningIn: Boolean = false,
    val isSignInSuccessful: Boolean = false,
    val signInError: String = ""
)
