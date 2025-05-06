package com.github.sendiko.penghitungsembako.sembako.form.presentation

sealed interface FormEvent {
    data class OnNameChanged(val name: String): FormEvent
    data class OnUnitChanged(val unit: String): FormEvent
    data class OnPricePerUnitChanged(val pricePerUnit: String): FormEvent
    object OnSave: FormEvent
}