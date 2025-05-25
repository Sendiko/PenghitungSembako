package com.github.sendiko.penghitungsembako.profile.presentation.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun formatRupiah(amount: Double): String {
    // Handle negative numbers by formatting the positive value and prepending a minus sign.
    if (amount < 0) {
        return "-${formatRupiah(-amount)}"
    }

    // Define the thresholds for different denominations
    val miliar = 1_000_000_000.0 // 1,000,000,000
    val juta = 1_000_000.0       // 1,000,000
    val ribu = 1_000.0           // 1,000

    // Set up DecimalFormatSymbols for Indonesian locale (dot as thousands separator, comma as decimal separator)
    val symbols = DecimalFormatSymbols(Locale("id", "ID")).apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }

    return when {
        // If the amount is 1 Billion or more
        amount >= miliar -> {
            // Format the amount divided by a billion, showing up to two decimal places.
            // For example, 1.23456789 Miliar becomes 1,23 Miliar.
            val formattedValue = DecimalFormat("#,##0.##", symbols).format(amount / miliar)
            "Rp$formattedValue Miliar"
        }
        // If the amount is 1 Million or more
        amount >= juta -> {
            // Format the amount divided by a million, showing up to two decimal places.
            // For example, 1.234567 Juta becomes 1,23 Juta.
            val formattedValue = DecimalFormat("#,##0.##", symbols).format(amount / juta)
            "Rp$formattedValue Juta"
        }
        // If the amount is 1 Thousand or more
        amount >= ribu -> {
            // Format the amount divided by a thousand, showing up to two decimal places.
            // For example, 1.2345 Ribu becomes 1,23 Ribu.
            val formattedValue = DecimalFormat("#,##0.##", symbols).format(amount / ribu)
            "Rp$formattedValue Ribu"
        }
        // For amounts less than 1 Thousand (including zero)
        else -> {
            // Format the amount as a whole number (no decimal places for amounts less than 1000).
            val formattedValue = DecimalFormat("#,##0", symbols).format(amount)
            "Rp$formattedValue"
        }
    }
}