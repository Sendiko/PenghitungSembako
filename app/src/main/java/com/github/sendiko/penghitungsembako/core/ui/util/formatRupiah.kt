package com.github.sendiko.penghitungsembako.core.ui.util

fun String.toRupiah(): String {
    return try {
        val number = this.replace(Regex("[^\\d]"), "").toLong()
        "Rp%,d".format(number).replace(',', '.')
    } catch (e: NumberFormatException) {
        "Rp0"
    }
}
