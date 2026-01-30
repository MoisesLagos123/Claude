package com.finanzas.app.util

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {
    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("es", "MX"))

    fun Double.toCurrency(): String = currencyFormatter.format(this)

    fun Double.toCompactCurrency(): String {
        return when {
            this >= 1_000_000 -> "${currencyFormatter.format(this / 1_000_000)}M"
            this >= 1_000 -> "${currencyFormatter.format(this / 1_000)}K"
            else -> currencyFormatter.format(this)
        }
    }
}
