package com.github.sendiko.penghitungsembako.grocery.core.domain

import com.github.sendiko.penghitungsembako.grocery.form.data.dto.GroceryItem

data class Grocery(
    val id: Int,
    val name: String,
    val unit: String,
    val pricePerUnit: Int,
    val imageUrl: String,
) {
    companion object {
        fun convertFromResponse(groceryItem: GroceryItem) = Grocery(
            id = groceryItem.id,
            name = groceryItem.name,
            unit = groceryItem.unit,
            pricePerUnit = groceryItem.price,
            imageUrl = groceryItem.imageUrl
        )
    }
}
