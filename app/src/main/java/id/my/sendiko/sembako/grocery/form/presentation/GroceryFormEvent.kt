package id.my.sendiko.sembako.grocery.form.presentation

import android.graphics.Bitmap

sealed interface GroceryFormEvent {
    data class OnNameChanged(val name: String): GroceryFormEvent
    data class OnUnitChanged(val unit: String): GroceryFormEvent
    data class OnPricePerUnitChanged(val pricePerUnit: String): GroceryFormEvent
    data class OnDeleteClicked(val isDeleting: Boolean): GroceryFormEvent
    data class OnDropDownChanged(val isExpanding: Boolean): GroceryFormEvent
    data class OnImageChosen(val bitmap: Bitmap?): GroceryFormEvent
    object OnSave: GroceryFormEvent
    object OnDelete: GroceryFormEvent
}