package com.cureius.pocket.feature_transaction.presentation.add_transaction

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cureius.pocket.feature_account.domain.model.Account
import com.cureius.pocket.feature_pot.domain.model.Pot
import com.cureius.pocket.feature_pot.domain.use_case.PotUseCases
import com.cureius.pocket.feature_pot.presentation.pots.PotsViewModel
import com.cureius.pocket.feature_transaction.domain.model.InvalidTransactionException
import com.cureius.pocket.feature_transaction.domain.model.Transaction
import com.cureius.pocket.feature_transaction.domain.use_case.TransactionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases,
    private val potUseCases: PotUseCases,
    private val potsViewModel: PotsViewModel,
    savedStateHandle: SavedStateHandle
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

    private val _transactionKind = mutableStateOf(
        ""
    )
    val transactionKind: State<String> = _transactionKind

    private val _transactionAccount = mutableStateOf(
        null as String?
    )
    val transactionAccount: State<String?> = _transactionAccount

    private val _transactionPot = mutableStateOf(
        null as String?
    )
    val transactionPot: State<String?> = _transactionPot

    private val _transactionAmount = mutableStateOf(
        ""
    )
    val transactionAmount: State<String> = _transactionAmount

    private val _transactionDate = mutableStateOf(
        LocalDate.now()
    )
    val transactionDate: State<LocalDate> = _transactionDate

    private val _transactionTime = mutableStateOf(
        LocalTime.now()
    )
    val transactionTime: State<LocalTime> = _transactionTime

    private val _transactionBalance = mutableStateOf<String?>(
        null
    )
    val transactionBalance: State<String?> = _transactionBalance

    private val _pot = mutableStateOf<Pot?>(null)
    val pot: State<Pot?> = _pot

    private val _retrievedPot = mutableStateOf<Pot?>(null)
    val retrievedPot: State<Pot?> = _retrievedPot

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

            is AddTransactionEvent.EnteredKind -> {
                _transactionKind.value = event.value
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
                _transactionDate.value = event.value

            }

            is AddTransactionEvent.EnteredTime -> {
                _transactionTime.value = event.value
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

            is AddTransactionEvent.SelectedAccount -> {
                _account.value = event.value
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
                                type = transactionType.value,
                                amount = transactionAmount.value.ifBlank { "0" }.toDouble(),
                                balance = transactionBalance.value?.toDouble(),
                                date = transactionDate.value.toEpochDay(),
                                event_timestamp = LocalDateTime.of(
                                    transactionDate.value,
                                    transactionTime.value
                                ).toEpochSecond(ZoneOffset.UTC) * 1000,
                                timestamp = System.currentTimeMillis(),
                                account = account.value?.account_number,
                                pot = pot.value?.title,
                                id = currentTransactionId,
                                kind = transactionKind.value,
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