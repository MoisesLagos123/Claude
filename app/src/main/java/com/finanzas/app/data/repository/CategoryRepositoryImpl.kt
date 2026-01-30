package com.finanzas.app.data.repository

import androidx.compose.ui.graphics.Color
import com.finanzas.app.data.local.dao.CategoryDao
import com.finanzas.app.domain.model.Category
import com.finanzas.app.domain.model.TransactionType
import com.finanzas.app.domain.repository.CategoryRepository
import com.finanzas.app.util.toDomain
import com.finanzas.app.util.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getCategoriesByType(type: String): Flow<List<Category>> {
        return categoryDao.getCategoriesByType(type).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getById(id: Long): Category? {
        return categoryDao.getById(id)?.toDomain()
    }

    override suspend fun insert(category: Category): Long {
        return categoryDao.insert(category.toEntity())
    }

    override suspend fun update(category: Category) {
        categoryDao.update(category.toEntity())
    }

    override suspend fun delete(category: Category) {
        categoryDao.delete(category.toEntity())
    }

    override suspend fun initializeDefaultCategories() {
        if (categoryDao.getCount() > 0) return

        val defaults = listOf(
            // Gastos Fijos
            Category(name = "Renta", icon = "home", color = Color(0xFFE53935), type = TransactionType.EXPENSE_FIXED, isDefault = true, sortOrder = 0),
            Category(name = "Servicios", icon = "bolt", color = Color(0xFFFF9800), type = TransactionType.EXPENSE_FIXED, isDefault = true, sortOrder = 1),
            Category(name = "Suscripciones", icon = "subscriptions", color = Color(0xFF9C27B0), type = TransactionType.EXPENSE_FIXED, isDefault = true, sortOrder = 2),
            Category(name = "Seguros", icon = "shield", color = Color(0xFF607D8B), type = TransactionType.EXPENSE_FIXED, isDefault = true, sortOrder = 3),

            // Gastos Variables
            Category(name = "Alimentación", icon = "restaurant", color = Color(0xFF4CAF50), type = TransactionType.EXPENSE_VARIABLE, isDefault = true, sortOrder = 4),
            Category(name = "Transporte", icon = "directions_car", color = Color(0xFF2196F3), type = TransactionType.EXPENSE_VARIABLE, isDefault = true, sortOrder = 5),
            Category(name = "Entretenimiento", icon = "movie", color = Color(0xFFFF5722), type = TransactionType.EXPENSE_VARIABLE, isDefault = true, sortOrder = 6),
            Category(name = "Salud", icon = "local_hospital", color = Color(0xFFF44336), type = TransactionType.EXPENSE_VARIABLE, isDefault = true, sortOrder = 7),
            Category(name = "Educación", icon = "school", color = Color(0xFF3F51B5), type = TransactionType.EXPENSE_VARIABLE, isDefault = true, sortOrder = 8),
            Category(name = "Ropa", icon = "checkroom", color = Color(0xFFE91E63), type = TransactionType.EXPENSE_VARIABLE, isDefault = true, sortOrder = 9),
            Category(name = "Hogar", icon = "weekend", color = Color(0xFF795548), type = TransactionType.EXPENSE_VARIABLE, isDefault = true, sortOrder = 10),
            Category(name = "Otros Gastos", icon = "more_horiz", color = Color(0xFF9E9E9E), type = TransactionType.EXPENSE_VARIABLE, isDefault = true, sortOrder = 11),

            // Ingresos
            Category(name = "Salario", icon = "payments", color = Color(0xFF00C853), type = TransactionType.INCOME, isDefault = true, sortOrder = 12),
            Category(name = "Freelance", icon = "computer", color = Color(0xFF00BCD4), type = TransactionType.INCOME, isDefault = true, sortOrder = 13),
            Category(name = "Inversiones", icon = "trending_up", color = Color(0xFF8BC34A), type = TransactionType.INCOME, isDefault = true, sortOrder = 14),
            Category(name = "Otros Ingresos", icon = "account_balance_wallet", color = Color(0xFF009688), type = TransactionType.INCOME, isDefault = true, sortOrder = 15),
        )

        categoryDao.insertAll(defaults.map { it.toEntity() })
    }
}
