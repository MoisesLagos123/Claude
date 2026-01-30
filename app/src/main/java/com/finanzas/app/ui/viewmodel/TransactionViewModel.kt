package com.finanzas.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finanzas.app.domain.model.Category
import com.finanzas.app.domain.model.PaymentMethod
import com.finanzas.app.domain.model.RecurringPeriod
import com.finanzas.app.domain.model.Transaction
import com.finanzas.app.domain.model.TransactionType
import com.finanzas.app.domain.repository.CategoryRepository
import com.finanzas.app.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class TransactionFormState(
    val id: Long? = null,
    val amount: String = "",
    val type: TransactionType = TransactionType.EXPENSE_VARIABLE,
    val categoryId: Long? = null,
    val description: String = "",
    val date: LocalDate = LocalDate.now(),
    val paymentMethod: PaymentMethod = PaymentMethod.CARD,
    val isRecurring: Boolean = false,
    val recurringPeriod: RecurringPeriod? = null,
    val tags: String = "",
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    val transactions = transactionRepository.getAllTransactions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories = categoryRepository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentDescriptions = transactionRepository.getRecentDescriptions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _formState = MutableStateFlow(TransactionFormState())
    val formState: StateFlow<TransactionFormState> = _formState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Transaction>>(emptyList())
    val searchResults: StateFlow<List<Transaction>> = _searchResults.asStateFlow()

    fun loadTransaction(id: Long) {
        viewModelScope.launch {
            val transaction = transactionRepository.getById(id) ?: return@launch
            _formState.value = TransactionFormState(
                id = transaction.id,
                amount = transaction.amount.toString(),
                type = transaction.type,
                categoryId = transaction.category?.id,
                description = transaction.description,
                date = transaction.date,
                paymentMethod = transaction.paymentMethod,
                isRecurring = transaction.isRecurring,
                recurringPeriod = transaction.recurringPeriod,
                tags = transaction.tags.joinToString(", ")
            )
        }
    }

    fun updateFormState(newState: TransactionFormState) {
        _formState.value = newState
    }

    fun resetForm() {
        _formState.value = TransactionFormState()
    }

    fun saveTransaction() {
        val state = _formState.value
        val amount = state.amount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            _formState.value = state.copy(error = "Ingresa un monto válido")
            return
        }

        viewModelScope.launch {
            _formState.value = state.copy(isLoading = true)
            try {
                val category = state.categoryId?.let { categoryRepository.getById(it) }
                val transaction = Transaction(
                    id = state.id ?: 0,
                    amount = amount,
                    type = state.type,
                    category = category,
                    description = state.description,
                    date = state.date,
                    paymentMethod = state.paymentMethod,
                    isRecurring = state.isRecurring,
                    recurringPeriod = if (state.isRecurring) state.recurringPeriod else null,
                    tags = state.tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                )

                if (state.id != null) {
                    transactionRepository.update(transaction)
                } else {
                    transactionRepository.insert(transaction)
                }

                _formState.value = state.copy(isLoading = false, isSaved = true)
            } catch (e: Exception) {
                _formState.value = state.copy(
                    isLoading = false,
                    error = "Error al guardar: ${e.message}"
                )
            }
        }
    }

    fun deleteTransaction(id: Long) {
        viewModelScope.launch {
            transactionRepository.delete(id)
        }
    }

    fun search(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }
        viewModelScope.launch {
            transactionRepository.searchTransactions(query).collect {
                _searchResults.value = it
            }
        }
    }
}
