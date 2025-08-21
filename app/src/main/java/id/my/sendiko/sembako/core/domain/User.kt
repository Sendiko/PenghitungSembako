package id.my.sendiko.sembako.core.domain

import android.net.Uri

data class User(
    val id: String?,
    val username: String?,
    val email: String?,
    val profileUrl: Uri?,
)
