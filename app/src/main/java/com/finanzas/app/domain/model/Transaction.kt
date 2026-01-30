package com.finanzas.app.domain.model

import java.time.LocalDate

data class Transaction(
    val id: Long = 0,
    val amount: Double,
    val type: TransactionType,
    val category: Category?,
    val description: String,
    val date: LocalDate,
    val paymentMethod: PaymentMethod,
    val isRecurring: Boolean = false,
    val recurringPeriod: RecurringPeriod? = null,
    val tags: List<String> = emptyList(),
    val importedFrom: String? = null
)

enum class TransactionType(val displayName: String) {
    INCOME("Ingreso"),
    EXPENSE_FIXED("Gasto Fijo"),
    EXPENSE_VARIABLE("Gasto Variable")
}

enum class PaymentMethod(val displayName: String) {
    CASH("Efectivo"),
    CARD("Tarjeta"),
    TRANSFER("Transferencia"),
    OTHER("Otro")
}

enum class RecurringPeriod(val displayName: String) {
    WEEKLY("Semanal"),
    MONTHLY("Mensual"),
    YEARLY("Anual")
}
