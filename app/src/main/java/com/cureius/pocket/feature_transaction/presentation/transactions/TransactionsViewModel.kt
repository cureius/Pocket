package com.cureius.pocket.feature_transaction.presentation.transactions

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cureius.pocket.feature_transaction.domain.model.Transaction
import com.cureius.pocket.feature_transaction.domain.use_case.TransactionUseCases
import com.cureius.pocket.feature_transaction.domain.util.OrderType
import com.cureius.pocket.feature_transaction.domain.util.TransactionOrder
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

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if (!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }

    init {
        getTransactions(TransactionOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: TransactionsEvent) {
        when (event) {
            is TransactionsEvent.Order -> {
                if (state.value.transactionOrder::class == event.transactionOrder::class &&
                    state.value.transactionOrder.orderType == event.transactionOrder.orderType
                ) {
                    return
                }
                getTransactions(event.transactionOrder)
            }
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
            is TransactionsEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSelectionVisible = !state.value.isOrderSelectionVisible
                )
            }

        }
    }

    private fun getTransactions(transactionOrder: TransactionOrder) {
        getTransactionsJob?.cancel()
        getTransactionsJob = transactionUseCases.getTransactions(transactionOrder).onEach { transactions ->
            _state.value = state.value.copy(
                transactions = transactions,
                transactionOrder= transactionOrder
            )
        }.launchIn(viewModelScope)
    }
}