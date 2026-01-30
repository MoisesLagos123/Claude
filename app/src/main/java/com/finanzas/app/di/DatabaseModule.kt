package com.finanzas.app.di

import android.content.Context
import androidx.room.Room
import com.finanzas.app.data.local.dao.BudgetDao
import com.finanzas.app.data.local.dao.CategoryDao
import com.finanzas.app.data.local.dao.TransactionDao
import com.finanzas.app.data.local.database.FinanzasDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FinanzasDatabase {
        return Room.databaseBuilder(
            context,
            FinanzasDatabase::class.java,
            "finanzas_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideTransactionDao(database: FinanzasDatabase): TransactionDao {
        return database.transactionDao()
    }

    @Provides
    fun provideCategoryDao(database: FinanzasDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    fun provideBudgetDao(database: FinanzasDatabase): BudgetDao {
        return database.budgetDao()
    }
}
