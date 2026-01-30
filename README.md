# Finanzas App

Aplicacion nativa de Android para gestion de finanzas personales, construida con Kotlin y Jetpack Compose.

## Funcionalidades

- **Dashboard**: Balance mensual, resumen de ingresos/gastos, graficos por categoria
- **Transacciones**: Registro de gastos fijos, variables e ingresos con categorias, etiquetas y metodos de pago
- **Analisis**: Reportes con distribucion de gastos, top categorias, comparativas
- **Importacion CSV**: Asistente paso a paso para importar movimientos bancarios
- **Configuracion**: Tema claro/oscuro, autenticacion biometrica, presupuestos, exportacion

## Stack Tecnologico

- **Lenguaje**: Kotlin
- **UI**: Jetpack Compose + Material Design 3
- **Arquitectura**: MVVM + Clean Architecture
- **Base de datos**: Room
- **Inyeccion de dependencias**: Hilt
- **Async**: Coroutines + Flow
- **Navegacion**: Navigation Compose

## Estructura del Proyecto

```
app/src/main/java/com/finanzas/app/
├── data/
│   ├── local/
│   │   ├── dao/          # DAOs de Room
│   │   ├── entity/       # Entidades de la BD
│   │   ├── database/     # Configuracion de Room
│   │   └── converter/    # TypeConverters
│   └── repository/       # Implementaciones de repositorios
├── domain/
│   ├── model/            # Modelos de dominio
│   └── repository/       # Interfaces de repositorios
├── di/                   # Modulos de Hilt
├── ui/
│   ├── theme/            # Material 3 theme
│   ├── navigation/       # NavGraph y scaffold
│   ├── viewmodel/        # ViewModels
│   └── screens/          # Pantallas de la app
│       ├── dashboard/
│       ├── transactions/
│       ├── addtransaction/
│       ├── reports/
│       ├── settings/
│       └── import_data/
└── util/                 # Utilidades (mappers, formatters)
```

## Pantallas

1. **Dashboard** - Resumen visual del mes con balance, ingresos vs gastos, gastos por categoria
2. **Transacciones** - Lista agrupada por fecha con busqueda y swipe-to-delete
3. **Agregar Transaccion** - Formulario con tipo, monto, categoria, fecha, metodo de pago, recurrencia
4. **Analisis** - Distribucion de gastos, top categorias, filtros por periodo
5. **Importar** - Asistente de 4 pasos para importar CSV/Excel
6. **Configuracion** - Tema, seguridad, categorias, presupuestos, respaldo

## Como Ejecutar

1. Abre el proyecto en Android Studio
2. Sincroniza las dependencias de Gradle
3. Ejecuta en emulador o dispositivo fisico (minSdk 26 / Android 8.0+)

## Licencia

MIT
