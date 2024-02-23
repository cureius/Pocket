package com.cureius.pocket.feature_transaction.presentation.transactions

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cureius.pocket.feature_account.domain.model.Account
import com.cureius.pocket.feature_account.domain.use_case.AccountUseCases
import com.cureius.pocket.feature_transaction.domain.model.Transaction
import com.cureius.pocket.feature_transaction.domain.use_case.TransactionUseCases
import com.cureius.pocket.feature_transaction.domain.util.OrderType
import com.cureius.pocket.feature_transaction.domain.util.TransactionOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneOffset
import javax.inject.Inject


@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases,
    private val accountUseCases: AccountUseCases
) : ViewModel() {


    private val _state = mutableStateOf(TransactionsState())
    val state: State<TransactionsState> = _state

    private val _accountsState = mutableStateOf(listOf<Account>())
    val accountsState: State<List<Account>> = _accountsState


    private var recentlyDeletedTransaction: Transaction? = null
    private var getTransactionsJob: Job? = null
    private var getTransactionsForDateRangeJob: Job? = null
    private var getTransactionsCreatedOnCurrentMonthJob: Job? = null
    private var getTransactionsCreatedOnCurrentMonthForAccountsJob: Job? = null
    private var getAccountsJob: Job? = null


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
        val currentDate = LocalDate.now() // Get the current date

        // Get the start of the month
        val startOfMonth = currentDate.withDayOfMonth(1)
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()

        // Get the end of the month
        val endOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth())
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()

        getAccounts()
        getTransactions(TransactionOrder.Date(OrderType.Descending))
        getTransactionsForDateRange(TransactionOrder.Date(OrderType.Descending), startOfMonth, endOfMonth)
        getTransactionsCreatedOnCurrentMonth(TransactionOrder.Date(OrderType.Descending))
        getTransactionsCreatedOnCurrentMonthForAccounts(TransactionOrder.Date(OrderType.Descending))
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
            println("TransactionsViewModel.getTransactions: transactions: ${transactions.size}")
            _state.value = state.value.copy(
                transactions = transactions,
                transactionOrder= transactionOrder
            )
        }.launchIn(viewModelScope)
    }
    private fun getTransactionsForDateRange(transactionOrder: TransactionOrder, start: Long, end: Long) {
        getTransactionsForDateRangeJob?.cancel()
        getTransactionsForDateRangeJob = transactionUseCases.getTransactionsForDateRange(transactionOrder, start, end).onEach { transactions ->
            println("TransactionsViewModel.getTransactionsForDateRange: transactions:  ${transactions.size}")
            _state.value = state.value.copy(
                transactionsForRange = transactions,
                transactionOrder= transactionOrder
            )
        }.launchIn(viewModelScope)
    }

    private fun getTransactionsCreatedOnCurrentMonth(transactionOrder: TransactionOrder) {
        getTransactionsCreatedOnCurrentMonthJob?.cancel()
        getTransactionsCreatedOnCurrentMonthJob =
            transactionUseCases.getTransactionsCreatedOnCurrentMonth(transactionOrder)
                .onEach { transactions ->
                    println("TransactionsViewModel.getTransactionsCreatedOnCurrentMonth: transactions:  ${transactions.size}")
                    _state.value = state.value.copy(
                        transactionsOnCurrentMonth = transactions,
                        transactionOrder = transactionOrder
                    )
                }.launchIn(viewModelScope)
    }

    private fun getTransactionsCreatedOnCurrentMonthForAccounts(transactionOrder: TransactionOrder) {
        getTransactionsCreatedOnCurrentMonthForAccountsJob?.cancel()
        getTransactionsCreatedOnCurrentMonthForAccountsJob =
            transactionUseCases.getTransactionsCreatedOnCurrentMonth(transactionOrder)
                .onEach { transactions ->
                    _state.value = state.value.copy(
                        transactionsOnCurrentMonthForAccounts = transactions.filter { it.account in accountsState.value.map { account: Account -> account.account_number } },
                        transactionOrder = transactionOrder
                    )
                    println("TransactionsViewModel.getTransactionsCreatedOnCurrentMonthForAccounts: transactions 2:  ${transactions.filter { it.account in accountsState.value.map { account: Account -> account.account_number } }.size}")
                }.launchIn(viewModelScope)
    }


    private fun getAccounts() {
        getAccountsJob?.cancel()
        getAccountsJob = accountUseCases.getAccounts().onEach { accounts ->
            _accountsState.value = accounts
        }.launchIn(viewModelScope)
    }
}