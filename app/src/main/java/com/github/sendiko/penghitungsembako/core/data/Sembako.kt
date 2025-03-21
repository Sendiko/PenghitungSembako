package com.github.sendiko.penghitungsembako.core.data

import androidx.annotation.DrawableRes

data class Sembako(
    val name: String,
    val unit: String,
    val pricePerUnit: Double,
    @DrawableRes val image: Int
)
