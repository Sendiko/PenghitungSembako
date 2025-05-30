package com.github.sendiko.penghitungsembako.grocery.form.presentation

import android.graphics.Bitmap
import com.github.sendiko.penghitungsembako.core.domain.User

data class FormState(
    val id: Int? = null,
    val name: String = "",
    val nameMessage: String = "",
    val unit: String = "",
    val unitMessage: String = "",
    val pricePerUnit: String = "",
    val pricePerUnitMessage: String = "",
    val message: String = "",
    val isSaved: Boolean = false,
    val isDeleted: Boolean = false,
    val isDeleting: Boolean = false,
    val isExpanding: Boolean = false,
    val bitmap: Bitmap? = null,
    val imageUrl: String = "",
    val bitmapMessage: String = "",
    val isLoading: Boolean = false,
    val user: User = User(0, "", "", "")
)
