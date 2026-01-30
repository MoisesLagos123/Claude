package com.finanzas.app.di

import com.finanzas.app.data.repository.CategoryRepositoryImpl
import com.finanzas.app.data.repository.TransactionRepositoryImpl
import com.finanzas.app.domain.repository.CategoryRepository
import com.finanzas.app.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        impl: TransactionRepositoryImpl
    ): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        impl: CategoryRepositoryImpl
    ): CategoryRepository
}
