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
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class UpdateTransactionViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases,
    private val potUseCases: PotUseCases,
    private val potsViewModel: PotsViewModel,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _dialogVisibility = mutableStateOf(false)
    val dialogVisibility: State<Boolean> = _dialogVisibility

    private val _potTitle = mutableStateOf("")
    val potTitle: State<String> = _potTitle

    private val _transaction = mutableStateOf<Transaction?>(
        null
    )
    val transaction: State<Transaction?> = _transaction


    private val _transactionTitle = mutableStateOf(
        ""
    )
    val transactionTitle: State<String> = _transactionTitle

    private val _transactionType = mutableStateOf(
        ""
    )
    val transactionType: State<String> = _transactionType

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

    private val _transactionBalance = mutableStateOf(
        ""
    )
    val transactionBalance: State<String> = _transactionBalance

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

    fun onEvent(event: UpdateTransactionEvent) {
        when (event) {
            is UpdateTransactionEvent.ToggleAddTransactionDialog -> {
                _dialogVisibility.value = !_dialogVisibility.value
            }

            is UpdateTransactionEvent.PickTransaction -> {
                _transaction.value = event.value
            }

            is UpdateTransactionEvent.EnteredTitle -> {
                _transactionTitle.value = event.value
            }

            is UpdateTransactionEvent.EnteredType -> {
                _transactionType.value = event.value

            }

            is UpdateTransactionEvent.ChangeTypeFocus -> {
                _transactionType.value = (!event.focusState.isFocused).toString()

            }

            is UpdateTransactionEvent.EnteredAccount -> {
                _transactionAccount.value = event.value

            }

            is UpdateTransactionEvent.ChangeAccountFocus -> {
                _transactionAccount.value = (!event.focusState.isFocused).toString()

            }

            is UpdateTransactionEvent.EnteredAmount -> {
                _transactionAmount.value = event.value

            }

            is UpdateTransactionEvent.ChangeAmountFocus -> {
                _transactionAmount.value = (!event.focusState.isFocused).toString()

            }

            is UpdateTransactionEvent.EnteredDate -> {
                _transactionDate.value = event.value

            }

            is UpdateTransactionEvent.EnteredTime -> {
                _transactionTime.value = event.value

            }

            is UpdateTransactionEvent.EnteredBalance -> {
                _transactionBalance.value = event.value.toString()

            }

            is UpdateTransactionEvent.ChangeBalanceFocus -> {
                _transactionBalance.value = (!event.focusState.isFocused).toString()

            }

            is UpdateTransactionEvent.EnteredTransactionsList -> {
                _transactionsList.value = transactionsList.value + event.value
            }

            is UpdateTransactionEvent.SelectedPot -> {
                _pot.value = event.value
            }

            is UpdateTransactionEvent.SelectedAccount -> {
                _account.value = event.value
            }

            is UpdateTransactionEvent.ChangeColor -> {
                _transactionColor.value = event.color
            }

            is UpdateTransactionEvent.UpdateTransaction -> {
                viewModelScope.launch {
                    try {
                        transaction.value?.let {
                            transactionUseCases.updateTransaction(
                                it.copy(
                                    pot = pot.value?.title,
                                )
                            )
                        }
                        _eventFlow.emit(UiEvent.SaveTransaction)
                    } catch (e: InvalidTransactionException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                message = e.message ?: "Cant Update Transaction"
                            )
                        )
                    }
                }
            }
        }
    }

}