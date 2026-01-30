package com.finanzas.app.domain.model

data class DashboardData(
    val totalIncome: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val balance: Double = 0.0,
    val expensesByCategory: List<CategoryExpense> = emptyList(),
    val recentTransactions: List<Transaction> = emptyList(),
    val monthlyTrend: List<MonthlyAmount> = emptyList()
)

data class CategoryExpense(
    val category: Category?,
    val amount: Double,
    val percentage: Float
)

data class MonthlyAmount(
    val month: String,
    val income: Double,
    val expenses: Double
)
