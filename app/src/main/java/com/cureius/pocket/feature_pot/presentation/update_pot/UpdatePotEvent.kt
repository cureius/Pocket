package com.cureius.pocket.feature_pot.presentation.update_pot

import com.cureius.pocket.feature_pot.domain.model.Pot

sealed class UpdatePotEvent {
    data class EnteredTitle(val value: String) : UpdatePotEvent()
    data class SelectPot(val value: Pot) : UpdatePotEvent()
    data class ToggledIsTemporary(val value: Boolean) : UpdatePotEvent()
    data class ToggledIsDefault(val value: Boolean) : UpdatePotEvent()
    data class EnteredValidity(val value: Long) : UpdatePotEvent()
    data class SelectedIcon(val value: String) : UpdatePotEvent()
    data class SelectedType(val value: String) : UpdatePotEvent()
    object ToggleUpdateAccountDialog : UpdatePotEvent()

    object SavePots : UpdatePotEvent()
    object SavePot : UpdatePotEvent()
    object DeletePot : UpdatePotEvent()

}