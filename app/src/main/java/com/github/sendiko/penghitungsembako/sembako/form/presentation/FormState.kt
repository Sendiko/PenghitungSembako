package com.github.sendiko.penghitungsembako.sembako.form.presentation

data class FormState(
    val id: Int? = null,
    val name: String = "",
    val unit: String = "",
    val pricePerUnit: String = "",
    val message: String = "",
    val isSaved: Boolean = false,
    val isDeleted: Boolean = false,
    val isDeleting: Boolean = false,
)
