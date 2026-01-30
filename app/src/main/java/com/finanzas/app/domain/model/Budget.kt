package com.finanzas.app.domain.model

data class Budget(
    val id: Long = 0,
    val categoryId: Long,
    val amount: Double,
    val period: RecurringPeriod,
    val alertThreshold: Double = 0.8,
    val spent: Double = 0.0
) {
    val remaining: Double get() = amount - spent
    val percentage: Float get() = if (amount > 0) (spent / amount).toFloat() else 0f
    val isOverBudget: Boolean get() = spent > amount
    val isNearLimit: Boolean get() = percentage >= alertThreshold
}
