package id.my.sendiko.sembako.history.presentation

import id.my.sendiko.sembako.user.core.domain.User
import id.my.sendiko.sembako.history.domain.History
import id.my.sendiko.sembako.store.core.domain.Store

data class HistoryState(
    val isLoading: Boolean = false,
    val message: String = "",
    val histories: List<History> = emptyList(),
    val user: User? = null,
    val stores: List<Store> = emptyList(),
    val selectedStore: Store? = null,
)
