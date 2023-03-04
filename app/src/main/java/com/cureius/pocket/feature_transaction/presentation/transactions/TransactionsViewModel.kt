package com.cureius.pocket.feature_transaction.presentation.transactions

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cureius.pocket.feature_transaction.domain.model.Transaction
import com.cureius.pocket.feature_transaction.domain.use_case.TransactionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases
) : ViewModel() {


    private val _state = mutableStateOf(TransactionsState())
    val state: State<TransactionsState> = _state
    private var recentlyDeletedTransaction: Transaction? = null
    private var getTransactionsJob: Job? = null

    init {
        getTransactions()
    }

    fun onEvent(event: TransactionsEvent) {
        when (event) {
            is TransactionsEvent.DeleteTransaction -> {
                viewModelScope.launch {
                    transactionUseCases.deleteTransaction(event.transaction)
                    recentlyDeletedTransaction = event.transaction
                }
            }
            is TransactionsEvent.RestoreTransaction -> {
                viewModelScope.launch {
                    recentlyDeletedTransaction?.let { transactionUseCases.addTransaction(it) }
                    recentlyDeletedTransaction = null
                }
            }

        }
    }

    private fun getTransactions() {
        getTransactionsJob?.cancel()
        getTransactionsJob = transactionUseCases.getTransactions().onEach { transactions ->
            _state.value = state.value.copy(
                transactions = transactions,
            )
        }.launchIn(viewModelScope)
    }
}