package id.my.sendiko.sembako.history.domain

import id.my.sendiko.sembako.history.data.HistoryEntity
import id.my.sendiko.sembako.history.data.dto.HistoryItem

data class History(
    val quantity: Int,
    val totalPrice: Int,
    val groceryName: String,
) {

    fun toHistoryEntity(): HistoryEntity {
        return HistoryEntity(
            quantity = this.quantity,
            totalPrice = this.totalPrice,
            groceryName = this.groceryName,
        )
    }
    companion object {
        fun fromHistoryItem(historyItem: HistoryItem): History {
            return History(
                quantity = historyItem.quantity,
                totalPrice = historyItem.totalPrice,
                groceryName = historyItem.grocery.name,
            )
        }

        fun fromHistoryEntity(historyEntity: HistoryEntity): History {
            return History(
                quantity = historyEntity.quantity,
                totalPrice = historyEntity.totalPrice,
                groceryName = historyEntity.groceryName,
            )
        }
    }
}
