package com.cureius.pocket.feature_transaction.presentation.add_transaction

import androidx.compose.ui.focus.FocusState
import com.cureius.pocket.feature_account.presentation.add_account.AddAccountEvent
import com.cureius.pocket.feature_transaction.domain.model.Transaction

sealed class AddTransactionEvent {
    data class EnteredType(val value: String) : AddTransactionEvent()
    data class ChangeTypeFocus(val focusState: FocusState) : AddTransactionEvent()

    data class EnteredAccount(val value: String) : AddTransactionEvent()
    data class ChangeAccountFocus(val focusState: FocusState) : AddTransactionEvent()

    data class EnteredAmount(val value: Double) : AddTransactionEvent()
    data class ChangeAmountFocus(val focusState: FocusState) : AddTransactionEvent()

    data class EnteredDate(val value: Long) : AddTransactionEvent()
    data class ChangeDateFocus(val focusState: FocusState) : AddTransactionEvent()

    data class EnteredBalance(val value: Double) : AddTransactionEvent()
    data class ChangeBalanceFocus(val focusState: FocusState) : AddTransactionEvent()

    data class ChangeColor(val color: Int) : AddTransactionEvent()
    object SaveTransaction : AddTransactionEvent()

    data class EnteredTransactionsList(val value: List<Transaction>) : AddTransactionEvent()
    object SaveAllTransactions : AddTransactionEvent()
    object ToggleAddTransactionDialog : AddTransactionEvent()


}
