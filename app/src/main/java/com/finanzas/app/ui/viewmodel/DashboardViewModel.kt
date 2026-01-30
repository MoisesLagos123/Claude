package com.finanzas.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finanzas.app.domain.model.CategoryExpense
import com.finanzas.app.domain.model.DashboardData
import com.finanzas.app.domain.model.MonthlyAmount
import com.finanzas.app.domain.model.Transaction
import com.finanzas.app.domain.repository.CategoryRepository
import com.finanzas.app.domain.repository.TransactionRepository
import com.finanzas.app.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _currentMonth = MutableStateFlow(LocalDate.now())

    val dashboardData: StateFlow<DashboardData> = run {
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

            val expenseTransactions = transactions.filter {
                it.type.name.startsWith("EXPENSE")
            }

            val groupedByCategory = expenseTransactions.groupBy { it.category?.id }
            val totalExpenseAmount = expenseTransactions.sumOf { it.amount }

            val expensesByCategory = groupedByCategory.map { (categoryId, txns) ->
                val category = categories.find { it.id == categoryId }
                val amount = txns.sumOf { it.amount }
                CategoryExpense(
                    category = category,
                    amount = amount,
                    percentage = if (totalExpenseAmount > 0) {
                        (amount / totalExpenseAmount).toFloat()
                    } else 0f
                )
            }.sortedByDescending { it.amount }

            DashboardData(
                totalIncome = totalIncome,
                totalExpenses = totalExpenses,
                balance = totalIncome - totalExpenses,
                expensesByCategory = expensesByCategory,
                recentTransactions = transactions.take(5)
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardData()
        )
    }

    init {
        viewModelScope.launch {
            categoryRepository.initializeDefaultCategories()
        }
    }
}
