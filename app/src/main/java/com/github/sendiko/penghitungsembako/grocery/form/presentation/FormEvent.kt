package com.github.sendiko.penghitungsembako.grocery.form.presentation

sealed interface FormEvent {
    data class OnNameChanged(val name: String): FormEvent
    data class OnUnitChanged(val unit: String): FormEvent
    data class OnPricePerUnitChanged(val pricePerUnit: String): FormEvent
    data class OnDeleteClicked(val isDeleting: Boolean): FormEvent
    data class OnDropDownChanged(val isExpanding: Boolean): FormEvent
    object OnSave: FormEvent
    object OnDelete: FormEvent
}