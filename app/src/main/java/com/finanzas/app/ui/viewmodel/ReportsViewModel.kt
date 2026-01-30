package com.finanzas.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finanzas.app.domain.model.CategoryExpense
import com.finanzas.app.domain.repository.CategoryRepository
import com.finanzas.app.domain.repository.TransactionRepository
import com.finanzas.app.util.DateUtils
import com.finanzas.app.util.DateUtils.toShortMonthString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

data class ReportsState(
    val totalIncome: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val expensesByCategory: List<CategoryExpense> = emptyList(),
    val monthlyData: List<MonthlyData> = emptyList(),
    val selectedPeriod: ReportPeriod = ReportPeriod.MONTH
)

data class MonthlyData(
    val label: String,
    val income: Double,
    val expenses: Double
)

enum class ReportPeriod(val displayName: String) {
    WEEK("Semana"),
    MONTH("Mes"),
    QUARTER("Trimestre"),
    YEAR("Año")
}

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _selectedPeriod = MutableStateFlow(ReportPeriod.MONTH)
    val selectedPeriod: StateFlow<ReportPeriod> = _selectedPeriod.asStateFlow()

    val reportsState: StateFlow<ReportsState> = run {
        val startOfMonth = DateUtils.getStartOfMonth()
        val endOfMonth = DateUtils.getEndOfMonth()

        combine(
            transactionRepository.getTotalIncomeInRange(startOfMonth, endOfMonth),
            transactionRepository.getTotalExpensesInRange(startOfMonth, endOfMonth),
            transactionRepository.getTransactionsByDateRange(startOfMonth, endOfMonth),
            categoryRepository.getAllCategories()
        ) { income, expenses, transactions, categories ->
            val totalIncome = income ?: 0.0
            val totalExpenses = expenses ?: 0.0

            val expenseTransactions = transactions.filter { it.type.name.startsWith("EXPENSE") }
            val totalExpAmount = expenseTransactions.sumOf { it.amount }

            val expensesByCategory = expenseTransactions
                .groupBy { it.category?.id }
                .map { (catId, txns) ->
                    val category = categories.find { it.id == catId }
                    val amount = txns.sumOf { it.amount }
                    CategoryExpense(
                        category = category,
                        amount = amount,
                        percentage = if (totalExpAmount > 0) (amount / totalExpAmount).toFloat() else 0f
                    )
                }
                .sortedByDescending { it.amount }

            // Generate last 6 months data
            val monthlyData = (5 downTo 0).map { monthsAgo ->
                val date = LocalDate.now().minusMonths(monthsAgo.toLong())
                MonthlyData(
                    label = date.toShortMonthString(),
                    income = 0.0, // Will be populated from actual data
                    expenses = 0.0
                )
            }

            ReportsState(
                totalIncome = totalIncome,
                totalExpenses = totalExpenses,
                expensesByCategory = expensesByCategory,
                monthlyData = monthlyData
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ReportsState()
        )
    }

    fun selectPeriod(period: ReportPeriod) {
        _selectedPeriod.value = period
    }
}
