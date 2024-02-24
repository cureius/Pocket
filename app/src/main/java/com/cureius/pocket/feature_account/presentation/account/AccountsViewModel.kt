package com.cureius.pocket.feature_account.presentation.account

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cureius.pocket.feature_account.domain.model.Account
import com.cureius.pocket.feature_account.domain.use_case.AccountUseCases
import com.cureius.pocket.feature_transaction.domain.use_case.TransactionUseCases
import com.cureius.pocket.feature_transaction.domain.util.OrderType
import com.cureius.pocket.feature_transaction.domain.util.TransactionOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val accountUseCases: AccountUseCases,
    private val transactionUseCases: TransactionUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(listOf<Account>())
    val state: State<List<Account>> = _state

    private val _accountNumberList = mutableStateOf(arrayOf<String>())
    val accountNumberList: State<Array<String>> = _accountNumberList

    private val _totalIncome = mutableStateOf(0.0)
    val totalIncome: State<Double> = _totalIncome

    private val _totalSpending = mutableStateOf(0.0)
    val totalSpending: State<Double> = _totalSpending

    private var getAccountsJob: Job? = null
    private var getAccountsBalanceJob: Job? = null

    init {
        getAccounts()
    }

    private fun getAccounts() {
        getAccountsJob?.cancel()
        getAccountsJob = accountUseCases.getAccounts().onEach { accounts ->
            Log.d(TAG, "getAccounts: $accounts")
            _state.value = accounts
            _accountNumberList.value = accounts.map { it -> it.account_number }.toTypedArray()
            transactionUseCases.getTransactionsOfAccount(
                TransactionOrder.Date(
                    OrderType.Descending
                ), _accountNumberList.value
            ).onEach { transactions ->
                println("Accounts " + _accountNumberList.value.size)
                Log.d("Account View model", "getAllAccountsBalance: $transactions")
                println("getAllAccountsBalance: $transactions")
                _totalIncome.value =
                    transactions.filter { it.type.equals("credited", true) }.sumOf { it.amount!! }
                _totalSpending.value =
                    transactions.filter { it.type.equals("debited", true) }.sumOf { it.amount!! }
            }.launchIn(viewModelScope)
        }.launchIn(viewModelScope)
    }

    private fun getAllAccountsBalance() {
        getAccountsBalanceJob?.cancel()
        getAccountsBalanceJob = transactionUseCases.getTransactionsOfAccount(
            TransactionOrder.Date(
                OrderType.Descending
            ), _accountNumberList.value
        ).onEach { transactions ->
            val accountNumbersArray: Array<String> =
                _state.value.map { it -> it.account_number }.toTypedArray()
            println("Accounts " + _accountNumberList.value.size)

            Log.d("Account View model", "getAllAccountsBalance: $transactions")
            println("getAllAccountsBalance: $transactions")
            _totalIncome.value =
                transactions.filter { it.type.equals("credited", true) }.sumOf { it.amount!! }
            _totalSpending.value =
                transactions.filter { it.type.equals("debited", true) }.sumOf { it.amount!! }
        }.launchIn(viewModelScope)
    }
}