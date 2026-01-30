package com.finanzas.app.domain.repository

import com.finanzas.app.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<Transaction>>
    fun getTransactionsByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<Transaction>>
    fun getTransactionsByType(type: String): Flow<List<Transaction>>
    fun getTransactionsByCategory(categoryId: Long): Flow<List<Transaction>>
    fun searchTransactions(query: String): Flow<List<Transaction>>
    fun getTotalExpensesInRange(startDate: LocalDate, endDate: LocalDate): Flow<Double?>
    fun getTotalIncomeInRange(startDate: LocalDate, endDate: LocalDate): Flow<Double?>
    fun getRecentDescriptions(): Flow<List<String>>
    suspend fun getById(id: Long): Transaction?
    suspend fun insert(transaction: Transaction): Long
    suspend fun update(transaction: Transaction)
    suspend fun delete(id: Long)
    suspend fun insertAll(transactions: List<Transaction>)
    suspend fun checkDuplicate(amount: Double, date: LocalDate, description: String): Boolean
}
