package id.my.sendiko.sembako.user.core.domain

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val profileUrl: String,
    val hasStore: Boolean
)