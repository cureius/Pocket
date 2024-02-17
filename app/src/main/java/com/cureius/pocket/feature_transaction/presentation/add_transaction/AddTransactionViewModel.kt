package com.cureius.pocket.feature_transaction.presentation.add_transaction

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cureius.pocket.feature_account.domain.model.Account
import com.cureius.pocket.feature_pot.domain.model.Pot
import com.cureius.pocket.feature_transaction.domain.model.InvalidTransactionException
import com.cureius.pocket.feature_transaction.domain.model.Transaction
import com.cureius.pocket.feature_transaction.domain.use_case.TransactionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases, savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _dialogVisibility = mutableStateOf(false)
    val dialogVisibility: State<Boolean> = _dialogVisibility

    private val _potTitle = mutableStateOf("")
    val potTitle: State<String> = _potTitle


    private val _transactionTitle = mutableStateOf(
        ""
    )
    val transactionTitle: State<String> = _transactionTitle

    private val _transactionType = mutableStateOf(
        ""
    )
    val transactionType: State<String> = _transactionType

    private val _transactionAccount = mutableStateOf(
        ""
    )
    val transactionAccount: State<String> = _transactionAccount

    private val _transactionAmount = mutableStateOf(
        ""
    )
    val transactionAmount: State<String> = _transactionAmount

    private val _transactionDate = mutableStateOf(
        ""
    )
    val transactionDate: State<String> = _transactionDate

    private val _transactionBalance = mutableStateOf(
        ""
    )
    val transactionBalance: State<String> = _transactionBalance

    private val _pot = mutableStateOf<Pot?>(null)
    val pot: State<Pot?> = _pot

    private val _account = mutableStateOf<Account?>(null)
    val account: State<Account?> = _account

    private val _transactionColor = mutableStateOf(Transaction.transactionColors.random().toArgb())
    val transactionColor: State<Int> = _transactionColor

    private val _transactionsList = mutableStateOf(listOf<Transaction>())
    val transactionsList: State<List<Transaction>> = _transactionsList

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentTransactionId: Long? = null

    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        object SaveTransaction : UiEvent()
    }


    fun onEvent(event: AddTransactionEvent) {
        when (event) {
            is AddTransactionEvent.ToggleAddTransactionDialog -> {
                _dialogVisibility.value = !_dialogVisibility.value
            }

            is AddTransactionEvent.EnteredTitle -> {
                _transactionTitle.value = event.value
            }

            is AddTransactionEvent.EnteredType -> {
                _transactionType.value = event.value

            }

            is AddTransactionEvent.ChangeTypeFocus -> {
                _transactionType.value = (!event.focusState.isFocused).toString()

            }

            is AddTransactionEvent.EnteredAccount -> {
                _transactionAccount.value = event.value

            }
            is AddTransactionEvent.ChangeAccountFocus -> {
                _transactionAccount.value = (!event.focusState.isFocused).toString()

            }

            is AddTransactionEvent.EnteredAmount -> {
                _transactionAmount.value = event.value

            }
            is AddTransactionEvent.ChangeAmountFocus -> {
                _transactionAmount.value = (!event.focusState.isFocused).toString()

            }

            is AddTransactionEvent.EnteredDate -> {
                _transactionDate.value = event.value.toString()

            }
            is AddTransactionEvent.ChangeDateFocus -> {
                _transactionDate.value = (!event.focusState.isFocused).toString()

            }

            is AddTransactionEvent.EnteredBalance -> {
                _transactionBalance.value = event.value.toString()

            }

            is AddTransactionEvent.ChangeBalanceFocus -> {
                _transactionBalance.value = (!event.focusState.isFocused).toString()

            }

            is AddTransactionEvent.EnteredTransactionsList -> {
                _transactionsList.value = transactionsList.value + event.value
            }

            is AddTransactionEvent.SelectedPot -> {
                _pot.value = event.value
            }

            is AddTransactionEvent.SaveAllTransactions -> {
                viewModelScope.launch {
                    try {
                        transactionUseCases.addTransactions(
                            transactionsList.value
                        )
                        _eventFlow.emit(UiEvent.SaveTransaction)
                    } catch (e: InvalidTransactionException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                message = e.message ?: "Cant Save Transaction"
                            )
                        )
                    }
                }
            }
            is AddTransactionEvent.ChangeColor -> {
                _transactionColor.value = event.color
            }
            is AddTransactionEvent.SaveTransaction -> {
                viewModelScope.launch {
                    try {
                        transactionUseCases.addTransaction(
                            Transaction(
                                title = transactionTitle.value,
//                                type = transactionType.value,
//                                account = transactionAccount.value,
                                amount = transactionAmount.value.toDouble(),
//                                balance = transactionBalance.value.toDouble(),
//                                date = transactionDate.value.toLong(),
                                timestamp = System.currentTimeMillis(),
//                                color = transactionColor.value,
                                id = currentTransactionId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveTransaction)
                    } catch (e: InvalidTransactionException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                message = e.message ?: "Cant Save Transaction"
                            )
                        )
                    }
                }
            }
        }
    }

}