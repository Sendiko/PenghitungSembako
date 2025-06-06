package id.my.sendiko.sembako.grocery.list.presentation

import id.my.sendiko.sembako.core.domain.User
import id.my.sendiko.sembako.core.preferences.UiMode
import id.my.sendiko.sembako.grocery.core.domain.Grocery

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
