package com.finanzas.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector? = null,
    val unselectedIcon: ImageVector? = null
) {
    data object Dashboard : Screen(
        route = "dashboard",
        title = "Inicio",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    data object Transactions : Screen(
        route = "transactions",
        title = "Movimientos",
        selectedIcon = Icons.Filled.Receipt,
        unselectedIcon = Icons.Outlined.Receipt
    )

    data object Reports : Screen(
        route = "reports",
        title = "Análisis",
        selectedIcon = Icons.Filled.Analytics,
        unselectedIcon = Icons.Outlined.Analytics
    )

    data object Settings : Screen(
        route = "settings",
        title = "Ajustes",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )

    data object AddTransaction : Screen(
        route = "add_transaction?id={id}",
        title = "Agregar Transacción"
    ) {
        fun createRoute(id: Long? = null): String {
            return if (id != null) "add_transaction?id=$id" else "add_transaction"
        }
    }

    data object ImportData : Screen(
        route = "import_data",
        title = "Importar Datos"
    )

    companion object {
        val bottomNavItems = listOf(Dashboard, Transactions, Reports, Settings)
    }
}
