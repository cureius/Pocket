package com.cureius.pocket.feature_transaction.presentation.add_transaction

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cureius.pocket.feature_account.presentation.add_account.AddAccountEvent
import com.cureius.pocket.feature_transaction.domain.model.InvalidTransactionException
import com.cureius.pocket.feature_transaction.domain.model.Transaction
import com.cureius.pocket.feature_transaction.domain.use_case.TransactionUseCases
import com.cureius.pocket.feature_transaction.domain.util.TransactionTextFieldState
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

    private val _transactionType = mutableStateOf(
        TransactionTextFieldState(
            hint = "Enter Title"
        )
    )
    val transactionType: State<TransactionTextFieldState> = _transactionType

    private val _transactionAccount = mutableStateOf(
        TransactionTextFieldState(
            hint = "Enter Account"
        )
    )
    val transactionAccount: State<TransactionTextFieldState> = _transactionAccount

    private val _transactionAmount = mutableStateOf(
        TransactionTextFieldState(
            hint = "Enter Amount"
        )
    )
    val transactionAmount: State<TransactionTextFieldState> = _transactionAmount

    private val _transactionDate = mutableStateOf(
        TransactionTextFieldState(
            hint = "Enter Date"
        )
    )
    val transactionDate: State<TransactionTextFieldState> = _transactionDate

    private val _transactionBalance = mutableStateOf(
        TransactionTextFieldState(
            hint = "Enter Balance"
        )
    )
    val transactionBalance: State<TransactionTextFieldState> = _transactionBalance

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


    init {
        savedStateHandle.get<Long>("transactionId")?.let { id ->
            if (id.toInt() != -1) {
                viewModelScope.launch {
                    transactionUseCases.getTransaction(id)?.also { transaction ->
                        currentTransactionId = transaction.id
                        _transactionType.value = transactionType.value.copy(
                            text = transaction.type, isHintVisible = false
                        )
                        _transactionAccount.value = transactionAccount.value.copy(
                            text = transaction.account, isHintVisible = false
                        )
                        _transactionAmount.value = transactionAmount.value.copy(
                            text = transaction.amount.toString(), isHintVisible = false
                        )
                        _transactionDate.value = transactionDate.value.copy(
                            text = transaction.date.toString(), isHintVisible = false
                        )
                        _transactionBalance.value = transactionBalance.value.copy(
                            text = transaction.balance.toString(), isHintVisible = false
                        )

                        _transactionColor.value = transaction.color
                    }
                }
            }
        }
    }


    fun onEvent(event: AddTransactionEvent) {
        when (event) {
            is AddTransactionEvent.ToggleAddTransactionDialog -> {
                _dialogVisibility.value = !_dialogVisibility.value
            }

            is AddTransactionEvent.EnteredType -> {
                _transactionType.value = transactionType.value.copy(
                    text = event.value
                )
            }
            is AddTransactionEvent.ChangeTypeFocus -> {
                _transactionType.value = transactionType.value.copy(
                    isHintVisible = !event.focusState.isFocused && transactionType.value.text.isBlank()
                )
            }

            is AddTransactionEvent.EnteredAccount -> {
                _transactionAccount.value = transactionAccount.value.copy(
                    text = event.value
                )
            }
            is AddTransactionEvent.ChangeAccountFocus -> {
                _transactionAccount.value = transactionAccount.value.copy(
                    isHintVisible = !event.focusState.isFocused && transactionAccount.value.text.isBlank()
                )
            }

            is AddTransactionEvent.EnteredAmount -> {
                _transactionAmount.value = transactionAmount.value.copy(
                    text = event.value.toString()
                )
            }
            is AddTransactionEvent.ChangeAmountFocus -> {
                _transactionAmount.value = transactionAmount.value.copy(
                    isHintVisible = !event.focusState.isFocused && transactionAmount.value.text.isBlank()
                )
            }

            is AddTransactionEvent.EnteredDate -> {
                _transactionDate.value = transactionDate.value.copy(
                    text = event.value.toString()
                )
            }
            is AddTransactionEvent.ChangeDateFocus -> {
                _transactionDate.value = transactionDate.value.copy(
                    isHintVisible = !event.focusState.isFocused && transactionDate.value.text.isBlank()
                )
            }

            is AddTransactionEvent.EnteredBalance -> {
                _transactionBalance.value = transactionBalance.value.copy(
                    text = event.value.toString()
                )
            }
            is AddTransactionEvent.ChangeBalanceFocus -> {
                _transactionBalance.value = transactionBalance.value.copy(
                    isHintVisible = !event.focusState.isFocused && transactionBalance.value.text.isBlank()
                )
            }

            is AddTransactionEvent.EnteredTransactionsList -> {
                _transactionsList.value = transactionsList.value + event.value
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
                                type = transactionType.value.text,
                                account = transactionAccount.value.text,
                                amount = transactionAmount.value.text.toDouble(),
                                balance = transactionBalance.value.text.toDouble(),
                                date = transactionDate.value.text.toLong(),
                                timestamp = System.currentTimeMillis(),
                                color = transactionColor.value,
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