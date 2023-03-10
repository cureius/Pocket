package com.cureius.pocket.feature_account.presentation.account

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cureius.pocket.feature_account.domain.model.Account
import com.cureius.pocket.feature_account.domain.model.Bank
import com.cureius.pocket.feature_account.domain.use_case.AccountUseCases
import com.cureius.pocket.feature_transaction.domain.model.InvalidTransactionException
import com.cureius.pocket.feature_transaction.domain.util.OrderType
import com.cureius.pocket.feature_transaction.domain.util.TransactionOrder
import com.cureius.pocket.feature_transaction.presentation.transactions.TransactionsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val accountUseCase: AccountUseCases, savedStateHandle: SavedStateHandle
) : ViewModel() {

    var isDialogShown by mutableStateOf(false)
        private set

    fun onAddClick() {
        isDialogShown = true
    }

    fun onDismissDialog() {
        isDialogShown = false
    }

    private val _accountHolderName = mutableStateOf("")
    val accountHolderName: State<String> = _accountHolderName

    private val _accountNumber = mutableStateOf("")
    val accountNumber: State<String> = _accountNumber

    private val _cardNumber = mutableStateOf("")
    val cardNumber: State<String> = _cardNumber

    private val _bank = mutableStateOf(
        Bank(
            null, null
        )
    )
    val bank: State<Bank> = _bank

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _state = mutableStateOf(listOf<Account>())
    val state: State<List<Account>> = _state
    private var getAccountsJob: Job? = null

    private var currentAccountId: Int? = null

    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        object SaveAccount : UiEvent()
    }

    init {
        getAccounts()
    }

    fun onEvent(event: AddAccountEvent) {
        when (event) {
            is AddAccountEvent.EnteredHolderName -> {
                _accountHolderName.value = event.value
            }
            is AddAccountEvent.SelectedBank -> {
                _bank.value = event.value
            }
            is AddAccountEvent.EnteredAccountNumber -> {
                _accountNumber.value = event.value
            }
            is AddAccountEvent.EnteredCardNumber -> {
                _cardNumber.value = event.value
            }

            is AddAccountEvent.SaveAccount -> {
                viewModelScope.launch {
                    try {
                        accountUseCase.addAccount(
                            Account(
                                accountNumber.value,
                                bank.value.name.toString(),
                                accountHolderName.value,
                                cardNumber.value,
                                is_primary = true,
                                timestamp = System.currentTimeMillis(),
                                id = currentAccountId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveAccount)
                    } catch (e: InvalidTransactionException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                message = e.message ?: "Cant Save Account"
                            )
                        )
                    }
                }
            }
        }
    }
    private fun getAccounts() {
        getAccountsJob?.cancel()
        getAccountsJob = accountUseCase.getAccounts().onEach { accounts ->
            _state.value = state.value + accounts
        }.launchIn(viewModelScope)
    }
}