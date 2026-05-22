package id.my.sendiko.sembako.store.presentation

import id.my.sendiko.sembako.store.domain.Store
import id.my.sendiko.sembako.user.core.domain.User

data class StoreState(
    val isLoading: Boolean = false,
    val message: String = "",
    val isError: Boolean = false,
    val stores: List<Store> = emptyList(),
    val user: User = User(
        id = 0,
        username = "",
        email = "",
        profileUrl = "",
        hasStore = false
    )
)
