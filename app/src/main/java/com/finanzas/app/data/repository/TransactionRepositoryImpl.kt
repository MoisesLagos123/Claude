package com.finanzas.app.data.repository

import com.finanzas.app.data.local.dao.CategoryDao
import com.finanzas.app.data.local.dao.TransactionDao
import com.finanzas.app.domain.model.Transaction
import com.finanzas.app.domain.repository.TransactionRepository
import com.finanzas.app.util.DateUtils.toEpochMilli
import com.finanzas.app.util.toDomain
import com.finanzas.app.util.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao
) : TransactionRepository {

    override fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions().map { entities ->
            entities.map { entity ->
                val category = entity.categoryId?.let { categoryDao.getById(it) }
                entity.toDomain(category?.toDomain())
            }
        }
    }

    override fun getTransactionsByDateRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByDateRange(
            startDate.toEpochMilli(),
            endDate.toEpochMilli()
        ).map { entities ->
            entities.map { entity ->
                val category = entity.categoryId?.let { categoryDao.getById(it) }
                entity.toDomain(category?.toDomain())
            }
        }
    }

    override fun getTransactionsByType(type: String): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByType(type).map { entities ->
            entities.map { entity ->
                val category = entity.categoryId?.let { categoryDao.getById(it) }
                entity.toDomain(category?.toDomain())
            }
        }
    }

    override fun getTransactionsByCategory(categoryId: Long): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByCategory(categoryId).map { entities ->
            entities.map { entity ->
                val category = entity.categoryId?.let { categoryDao.getById(it) }
                entity.toDomain(category?.toDomain())
            }
        }
    }

    override fun searchTransactions(query: String): Flow<List<Transaction>> {
        return transactionDao.searchTransactions(query).map { entities ->
            entities.map { entity ->
                val category = entity.categoryId?.let { categoryDao.getById(it) }
                entity.toDomain(category?.toDomain())
            }
        }
    }

    override fun getTotalExpensesInRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<Double?> {
        return transactionDao.getTotalExpensesInRange(
            startDate.toEpochMilli(),
            endDate.toEpochMilli()
        )
    }

    override fun getTotalIncomeInRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<Double?> {
        return transactionDao.getTotalIncomeInRange(
            startDate.toEpochMilli(),
            endDate.toEpochMilli()
        )
    }

    override fun getRecentDescriptions(): Flow<List<String>> {
        return transactionDao.getRecentDescriptions()
    }

    override suspend fun getById(id: Long): Transaction? {
        val entity = transactionDao.getById(id) ?: return null
        val category = entity.categoryId?.let { categoryDao.getById(it) }
        return entity.toDomain(category?.toDomain())
    }

    override suspend fun insert(transaction: Transaction): Long {
        return transactionDao.insert(transaction.toEntity())
    }

    override suspend fun update(transaction: Transaction) {
        transactionDao.update(transaction.toEntity())
    }

    override suspend fun delete(id: Long) {
        transactionDao.deleteById(id)
    }

    override suspend fun insertAll(transactions: List<Transaction>) {
        transactionDao.insertAll(transactions.map { it.toEntity() })
    }

    override suspend fun checkDuplicate(
        amount: Double,
        date: LocalDate,
        description: String
    ): Boolean {
        return transactionDao.checkDuplicate(amount, date.toEpochMilli(), description) > 0
    }
}
