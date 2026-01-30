package com.finanzas.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories",
    indices = [Index(value = ["name"], unique = true)]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val icon: String, // Material icon name
    val color: Long, // ARGB color as Long
    val type: String, // INCOME, EXPENSE_FIXED, EXPENSE_VARIABLE
    val parentId: Long? = null, // for subcategories
    val isDefault: Boolean = false,
    val sortOrder: Int = 0
)
