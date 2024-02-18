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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
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

    private val _transactionAccount = mutableStateOf(
        ""
    )
    val transactionAccount: State<String> = _transactionAccount

    private val _transactionPot = mutableStateOf(
        ""
    )
    val transactionPot: State<String> = _transactionAccount

    private val _transactionAmount = mutableStateOf(
        ""
    )
    val transactionAmount: State<String> = _transactionAmount

    private val _transactionDate = mutableStateOf(
        ""
    )
    val transactionDate: State<String> = _transactionDate

    private val _transactionTime = mutableStateOf(
        ""
    )
    val transactionTime: State<String> = _transactionTime

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

            is AddTransactionEvent.EnteredTime -> {
                _transactionTime.value = event.value.toString()

            }

            is AddTransactionEvent.ChangeTimeFocus -> {
                _transactionTime.value = (!event.focusState.isFocused).toString()

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
//                                balance = transactionBalance.value.toDouble(),
                                date = transactionDate.value.ifEmpty { "0" }.toLong(),
                                date_time = transactionDate.value + " | " + transactionTime.value,
                                timestamp = System.currentTimeMillis(),
//                                color = transactionColor.value,
                                account = account.value?.account_number,
                                pot = pot.value?.title,
                                id = currentTransactionId
                            )
                        )


//                        If a transaction is being created for the first time in the running month then create a new pot object and push it to db
//                        IF there is already a pot by this name for this running month then just update the pot values


                        // Get the current date
                        val currentDate = LocalDate.now()
                        // Get the last day of the current month
                        val lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth())
                        // Get midnight of the last day of the current month
                        val midnightLastDayOfMonth = lastDayOfMonth.atStartOfDay()
                        // Convert the LocalDateTime to a Unix timestamp (in seconds)
                        val validityTimestamp = midnightLastDayOfMonth.toEpochSecond(ZoneOffset.UTC)
                        // Now, you can call the DAO method to get the pots with the calculated validity timestamp
                        val pots = potsViewModel.validPots.value
                        val filteredPot = pots.firstOrNull { it.title == pot.value?.title }
                        if (filteredPot == null) {
                            if (transactionType.value != "Income") potUseCases.addPot(
                                Pot(
                                    title = pot.value?.title ?: "Default",
                                    weight = pot.value?.weight,
                                    capacity = pot.value?.capacity,
                                    amount = transactionAmount.value.toDouble(),
                                    type = pot.value?.type,
                                    filled = pot.value?.filled,
                                    is_template = false,
                                    is_monthly = true,
                                    is_temporary = true,
                                    is_default = pot.value?.is_default,
                                    validity = validityTimestamp,
                                    associated_account = pot.value?.associated_account,
                                    icon = pot.value?.icon,
                                    parent = pot.value?.parent,
                                    timestamp = System.currentTimeMillis(),
                                )
                            )
                        } else {
                            potUseCases.updatePot(
                                filteredPot.copy(
                                    amount = filteredPot.amount?.plus(transactionAmount.value.toDouble()),
                                    timestamp = System.currentTimeMillis()
                                )
                            )
                        }

//                        If it is a income kine of a transaction then it should be responsible for increasing the all pot capacity based on the weight of the pot
                        if (transactionType.value == "Income") {
                            val pots =
                                potsViewModel.validPots.value
                            pots.forEach {
                                potUseCases.updatePot(
                                    it.copy(
                                        capacity = if (it.capacity != null) {
                                            it.capacity.plus(
                                                it.weight?.times(
                                                    transactionAmount.value.toDouble()
                                                ) ?: 0.0
                                            )
                                        } else {
                                            it.weight?.times(
                                                transactionAmount.value.toDouble()
                                            )
                                        }, timestamp = System.currentTimeMillis()
                                    )
                                )
                            }
                        }

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