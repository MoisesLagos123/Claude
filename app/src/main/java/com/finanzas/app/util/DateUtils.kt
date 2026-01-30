package com.finanzas.app.util

import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {

    private val displayFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale("es", "MX"))
    private val monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es", "MX"))
    private val shortMonthFormatter = DateTimeFormatter.ofPattern("MMM", Locale("es", "MX"))

    fun LocalDate.toEpochMilli(): Long {
        return atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun LocalDate.toDisplayString(): String = format(displayFormatter)

    fun LocalDate.toMonthYearString(): String =
        format(monthYearFormatter).replaceFirstChar { it.uppercase() }

    fun LocalDate.toShortMonthString(): String =
        format(shortMonthFormatter).replaceFirstChar { it.uppercase() }

    fun getStartOfMonth(date: LocalDate = LocalDate.now()): LocalDate {
        return date.withDayOfMonth(1)
    }

    fun getEndOfMonth(date: LocalDate = LocalDate.now()): LocalDate {
        return date.withDayOfMonth(date.lengthOfMonth())
    }

    fun getStartOfYear(date: LocalDate = LocalDate.now()): LocalDate {
        return date.withDayOfYear(1)
    }

    fun getEndOfYear(date: LocalDate = LocalDate.now()): LocalDate {
        return date.withDayOfYear(date.lengthOfYear())
    }
}
