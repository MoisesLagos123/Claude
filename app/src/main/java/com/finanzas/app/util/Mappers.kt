package com.finanzas.app.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.finanzas.app.data.local.entity.CategoryEntity
import com.finanzas.app.data.local.entity.TransactionEntity
import com.finanzas.app.domain.model.Category
import com.finanzas.app.domain.model.PaymentMethod
import com.finanzas.app.domain.model.RecurringPeriod
import com.finanzas.app.domain.model.Transaction
import com.finanzas.app.domain.model.TransactionType
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun TransactionEntity.toDomain(category: Category?): Transaction {
    return Transaction(
        id = id,
        amount = amount,
        type = TransactionType.valueOf(type),
        category = category,
        description = description,
        date = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate(),
        paymentMethod = PaymentMethod.valueOf(paymentMethod),
        isRecurring = isRecurring,
        recurringPeriod = recurringPeriod?.let { RecurringPeriod.valueOf(it) },
        tags = if (tags.isEmpty()) emptyList() else tags.split(","),
        importedFrom = importedFrom
    )
}

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        amount = amount,
        type = type.name,
        categoryId = category?.id,
        description = description,
        date = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        paymentMethod = paymentMethod.name,
        isRecurring = isRecurring,
        recurringPeriod = recurringPeriod?.name,
        tags = tags.joinToString(","),
        importedFrom = importedFrom
    )
}

fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        icon = icon,
        color = Color(color.toULong()),
        type = TransactionType.valueOf(type),
        parentId = parentId,
        isDefault = isDefault,
        sortOrder = sortOrder
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        icon = icon,
        color = color.toArgb().toLong(),
        type = type.name,
        parentId = parentId,
        isDefault = isDefault,
        sortOrder = sortOrder
    )
}
