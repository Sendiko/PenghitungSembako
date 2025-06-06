package id.my.sendiko.sembako.history.presentation

import id.my.sendiko.sembako.core.domain.User
import id.my.sendiko.sembako.history.domain.History

data class HistoryState(
    val isLoading: Boolean = false,
    val message: String = "",
    val histories: List<History> = emptyList(),
    val user: User? = null,
)
