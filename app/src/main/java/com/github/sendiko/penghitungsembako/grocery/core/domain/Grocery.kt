package com.github.sendiko.penghitungsembako.grocery.core.domain

data class Grocery(
    val id: Int,
    val name: String,
    val unit: String,
    val pricePerUnit: Int,
    val imageUrl: String,
)
