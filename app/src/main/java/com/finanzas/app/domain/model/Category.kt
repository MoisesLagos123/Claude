package com.finanzas.app.domain.model

import androidx.compose.ui.graphics.Color

data class Category(
    val id: Long = 0,
    val name: String,
    val icon: String,
    val color: Color,
    val type: TransactionType,
    val parentId: Long? = null,
    val isDefault: Boolean = false,
    val sortOrder: Int = 0
)
