package com.github.sendiko.penghitungsembako.sembako.form.presentation

import com.github.sendiko.penghitungsembako.sembako.core.data.Sembako

data class FormState(
    val sembako: Sembako? = null,
    val name: String = "",
    val unit: String = "",
    val pricePerUnit: String = "",
    val message: String = ""
)
