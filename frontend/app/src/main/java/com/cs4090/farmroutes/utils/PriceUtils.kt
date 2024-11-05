package com.cs4090.farmroutes.utils

import java.text.NumberFormat
import java.util.Locale

fun formatPrice(amount: Double): String {
    /**
     * Format a double in a appropriate currency format.
     */
    val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
    return formatter.format(amount)
}