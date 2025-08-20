package id.my.sendiko.sembako.signin.presentation

data class SignInState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String = ""
)
