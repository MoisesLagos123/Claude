package com.finanzas.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["categoryId"]),
        Index(value = ["date"]),
        Index(value = ["type"])
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val type: String, // INCOME, EXPENSE_FIXED, EXPENSE_VARIABLE
    val categoryId: Long?,
    val description: String,
    val date: Long, // timestamp
    val paymentMethod: String, // CASH, CARD, TRANSFER, OTHER
    val isRecurring: Boolean = false,
    val recurringPeriod: String? = null, // WEEKLY, MONTHLY, YEARLY
    val tags: String = "", // comma-separated
    val importedFrom: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
