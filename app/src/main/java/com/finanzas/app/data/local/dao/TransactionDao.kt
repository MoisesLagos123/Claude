package com.finanzas.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.finanzas.app.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<TransactionEntity>)

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getById(id: Long): TransactionEntity?

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    fun getTransactionsByType(type: String): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE categoryId = :categoryId ORDER BY date DESC")
    fun getTransactionsByCategory(categoryId: Long): Flow<List<TransactionEntity>>

    @Query("""
        SELECT SUM(amount) FROM transactions
        WHERE type = :type AND date BETWEEN :startDate AND :endDate
    """)
    fun getTotalByTypeAndDateRange(type: String, startDate: Long, endDate: Long): Flow<Double?>

    @Query("""
        SELECT categoryId, SUM(amount) as total
        FROM transactions
        WHERE type LIKE 'EXPENSE%' AND date BETWEEN :startDate AND :endDate
        GROUP BY categoryId
        ORDER BY total DESC
    """)
    fun getExpensesByCategoryInRange(startDate: Long, endDate: Long): Flow<List<CategoryTotal>>

    @Query("""
        SELECT SUM(amount) FROM transactions
        WHERE type LIKE 'EXPENSE%' AND date BETWEEN :startDate AND :endDate
    """)
    fun getTotalExpensesInRange(startDate: Long, endDate: Long): Flow<Double?>

    @Query("""
        SELECT SUM(amount) FROM transactions
        WHERE type = 'INCOME' AND date BETWEEN :startDate AND :endDate
    """)
    fun getTotalIncomeInRange(startDate: Long, endDate: Long): Flow<Double?>

    @Query("""
        SELECT * FROM transactions
        WHERE description LIKE '%' || :query || '%'
        OR tags LIKE '%' || :query || '%'
        ORDER BY date DESC
    """)
    fun searchTransactions(query: String): Flow<List<TransactionEntity>>

    @Query("SELECT DISTINCT description FROM transactions ORDER BY date DESC LIMIT 20")
    fun getRecentDescriptions(): Flow<List<String>>

    @Query("""
        SELECT COUNT(*) FROM transactions
        WHERE amount = :amount AND date = :date AND description = :description
    """)
    suspend fun checkDuplicate(amount: Double, date: Long, description: String): Int
}

data class CategoryTotal(
    val categoryId: Long?,
    val total: Double
)
