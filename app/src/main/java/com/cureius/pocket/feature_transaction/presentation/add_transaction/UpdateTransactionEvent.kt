package com.cureius.pocket.feature_transaction.presentation.add_transaction

import androidx.compose.ui.focus.FocusState
import com.cureius.pocket.feature_account.domain.model.Account
import com.cureius.pocket.feature_pot.domain.model.Pot
import com.cureius.pocket.feature_transaction.domain.model.Transaction
import java.time.LocalDate
import java.time.LocalTime

sealed class UpdateTransactionEvent {
    data class PickTransaction(val value: Transaction) : UpdateTransactionEvent()
    data class EnteredTitle(val value: String) : UpdateTransactionEvent()
    data class EnteredType(val value: String) : UpdateTransactionEvent()
    data class ChangeTypeFocus(val focusState: FocusState) : UpdateTransactionEvent()

    data class EnteredAccount(val value: String) : UpdateTransactionEvent()
    data class ChangeAccountFocus(val focusState: FocusState) : UpdateTransactionEvent()

    data class SelectedPot(val value: Pot?) : UpdateTransactionEvent()
    data class SelectedAccount(val value: Account?) : UpdateTransactionEvent()
    data class EnteredAmount(val value: String) : UpdateTransactionEvent()
    data class ChangeAmountFocus(val focusState: FocusState) : UpdateTransactionEvent()

    data class EnteredDate(val value: LocalDate) : UpdateTransactionEvent()
    data class ChangeDateFocus(val focusState: FocusState) : UpdateTransactionEvent()

    data class EnteredTime(val value: LocalTime) : UpdateTransactionEvent()
    data class ChangeTimeFocus(val focusState: FocusState) : UpdateTransactionEvent()

    data class EnteredBalance(val value: Double) : UpdateTransactionEvent()
    data class ChangeBalanceFocus(val focusState: FocusState) : UpdateTransactionEvent()

    data class ChangeColor(val color: Int) : UpdateTransactionEvent()
    object UpdateTransaction : UpdateTransactionEvent()
    object UpdateTransactionTitle : UpdateTransactionEvent()

    data class EnteredTransactionsList(val value: List<Transaction>) : UpdateTransactionEvent()
    object SaveAllTransactions : UpdateTransactionEvent()
    object ToggleAddTransactionDialog : UpdateTransactionEvent()


}
