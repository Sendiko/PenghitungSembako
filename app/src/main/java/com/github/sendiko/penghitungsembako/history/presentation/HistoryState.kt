package com.github.sendiko.penghitungsembako.history.presentation

import com.github.sendiko.penghitungsembako.core.domain.User
import com.github.sendiko.penghitungsembako.history.domain.History

data class HistoryState(
    val isLoading: Boolean = false,
    val message: String = "",
    val histories: List<History> = emptyList(),
    val user: User? = null,
)
