package com.finanzas.app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.finanzas.app.data.local.converter.Converters
import com.finanzas.app.data.local.dao.BudgetDao
import com.finanzas.app.data.local.dao.CategoryDao
import com.finanzas.app.data.local.dao.TransactionDao
import com.finanzas.app.data.local.entity.BudgetEntity
import com.finanzas.app.data.local.entity.CategoryEntity
import com.finanzas.app.data.local.entity.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        CategoryEntity::class,
        BudgetEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class FinanzasDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun budgetDao(): BudgetDao
}
