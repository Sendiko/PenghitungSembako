package com.github.sendiko.penghitungsembako.grocery.list.presentation

import com.github.sendiko.penghitungsembako.core.domain.User
import com.github.sendiko.penghitungsembako.core.preferences.UiMode
import com.github.sendiko.penghitungsembako.grocery.core.domain.Grocery

data class ListState(
    val groceries: List<Grocery> = emptyList(),
    val grocery: Grocery? = null,
    val usingOns: Boolean = false,
    val quantity: String = "",
    val totalPrice: Double = 0.0,
    val message: String = "",
    val uiMode: UiMode = UiMode.GRID,
    val user: User? = null,
    val isLoading: Boolean = false,
)
