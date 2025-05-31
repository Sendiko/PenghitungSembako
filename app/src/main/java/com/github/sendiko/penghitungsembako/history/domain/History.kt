package com.github.sendiko.penghitungsembako.history.domain

import com.github.sendiko.penghitungsembako.history.data.dto.HistoryItem

data class History(
    val quantity: Int,
    val totalPrice: Int,
    val groceryName: String,
) {
    companion object {
        fun fromHistoryItem(historyItem: HistoryItem): History {
            return History(
                quantity = historyItem.quantity,
                totalPrice = historyItem.totalPrice,
                groceryName = historyItem.grocery.name,
            )
        }
    }
}
