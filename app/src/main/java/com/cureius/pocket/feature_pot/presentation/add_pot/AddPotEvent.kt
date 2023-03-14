package com.cureius.pocket.feature_pot.presentation.add_pot

import androidx.compose.ui.focus.FocusState
import com.cureius.pocket.feature_transaction.presentation.add_transaction.AddTransactionEvent

sealed class AddPotEvent {
    data class EnteredTitle(val value: String) : AddPotEvent()
    data class ToggledIsTemporary(val value: Boolean) : AddPotEvent()
    data class ToggledIsDefault(val value: Boolean) : AddPotEvent()
    data class EnteredValidity(val value: Long) : AddPotEvent()
    data class SelectedIcon(val value: String) : AddPotEvent()
    data class SelectedType(val value: String) : AddPotEvent()
    object SavePot : AddPotEvent()

}